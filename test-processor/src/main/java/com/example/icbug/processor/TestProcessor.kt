package com.example.icbug.processor

import com.example.annotations.ApolloType
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.ksp.originatingKSFiles
import com.squareup.kotlinpoet.ksp.writeTo
import com.squareup.kotlinpoet.ksp.toClassName

class TestProcessor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(ApolloType::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .filter(KSNode::validate)
            .forEach {
                val packageName = it.packageName
                it.annotations.forEach {
                    val apolloTypeClass = it.arguments
                        .first { it.name?.asString() == "apolloTypeClass" }.value as KSType
                    val fileSpec = getFileSpec(
                        packageName = packageName.asString(),
                        className = apolloTypeClass.toClassName(),
                        properties = (apolloTypeClass.declaration as KSClassDeclaration)
                            .getAllProperties().toList()
                    )
                    fileSpec.writeTo(
                        codeGenerator,
                        false,
                        fileSpec.originatingKSFiles()
                    )
                }
            }

        return emptyList()
    }

    private fun getFileSpec(
        packageName: String,
        className: ClassName,
        properties: List<KSPropertyDeclaration>,
    ): FileSpec {
        return FileSpec.builder(packageName, "${className.simpleName}Properties")
            .addFunction(
                FunSpec.builder("properties")
                    .receiver(className)
                    .returns(
                        ClassName("kotlin.collections", "List").parameterizedBy(STRING)
                    )
                    .addCode("return listOf(\n")
                    .addCode(CodeBlock.builder().indent().build())
                    .apply {
                        properties.forEach {
                            addStatement("%S,", it.simpleName.asString())
                        }
                    }
                    .addCode(CodeBlock.builder().unindent().build())
                    .addStatement(")")
                    .build()
            )
            .build()
    }
}
