package com.annevonwolffen.detektrules

import io.gitlab.arturbosch.detekt.api.Finding
import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.compileAndLint
import org.junit.Test

class MissingInternalModifierRuleTest {

    @Test
    fun `class in impl module without internal`() {
        val code = """
            package com.annevonwolffen.gallery_impl.domain

            import com.annevonwolffen.gallery_impl.presentation.Result
            import kotlinx.coroutines.flow.Flow

            interface ImagesInteractor {
                fun getImagesFlow(folder: String): Flow<Result<List<Image>>>
                suspend fun uploadImage(folder: String, image: Image): Result<String>
                suspend fun uploadFileToStorage(folder: String, image: Image)
                suspend fun deleteImage(folder: String, image: Image): Result<Unit>
                suspend fun deleteFileFromStorage(folder: String, image: Image)
            }
            
            object SomeObject {
            
                fun someFun() {
                    return Unit
                }
            }

            fun createImageFile(context: Context) {
                
            }
            
            private fun somePrivateFun(context: Context) {
                
            }
            
            fun String.extension(): String = ""
            
            const val FILE_NAME = ""
            val globalVal = ""

        """.trimIndent()

        val findings = MissingInternalModifierRule().compileAndLint(code)
        assertThat(findings).hasSize(6)
        assertFindingMessage(findings[0], "ImagesInteractor")
        assertFindingMessage(findings[1], "SomeObject")
        assertFindingMessage(findings[2], "createImageFile")
        assertFindingMessage(findings[3], "extension")
        assertFindingMessage(findings[4], "FILE_NAME")
        assertFindingMessage(findings[5], "globalVal")
    }

    @Test
    fun `class in api module without internal`() {
        val code = """
            package com.annevonwolffen.gallery_api.domain

            import com.annevonwolffen.gallery_impl.presentation.Result
            import kotlinx.coroutines.flow.Flow

            interface ImagesInteractor {
                fun getImagesFlow(folder: String): Flow<Result<List<Image>>>
                suspend fun uploadImage(folder: String, image: Image): Result<String>
                suspend fun uploadFileToStorage(folder: String, image: Image)
                suspend fun deleteImage(folder: String, image: Image): Result<Unit>
                suspend fun deleteFileFromStorage(folder: String, image: Image)
            }
        """.trimIndent()

        val findings = MissingInternalModifierRule().compileAndLint(code)
        assertThat(findings).hasSize(0)
    }

    @Test
    fun `class in impl module with internal`() {
        val code = """
            package com.annevonwolffen.gallery_impl.domain

            import com.annevonwolffen.gallery_impl.presentation.Result
            import kotlinx.coroutines.flow.Flow

            internal interface ImagesInteractor {
                fun getImagesFlow(folder: String): Flow<Result<List<Image>>>
                suspend fun uploadImage(folder: String, image: Image): Result<String>
                suspend fun uploadFileToStorage(folder: String, image: Image)
                suspend fun deleteImage(folder: String, image: Image): Result<Unit>
                suspend fun deleteFileFromStorage(folder: String, image: Image)
            }
        """.trimIndent()

        val findings = MissingInternalModifierRule().compileAndLint(code)
        assertThat(findings).hasSize(0)
    }

    private fun assertFindingMessage(finding: Finding, itemName: String) {
        assertThat(finding).hasMessage("$itemName $INTERNAL_IMPL_ISSUE_REPORT_MESSAGE")
    }

    companion object {
        const val INTERNAL_IMPL_ISSUE_REPORT_MESSAGE = """
            должен быть помечен модификатором доступа `internal`, так как находится внутри impl-модуля 
            и не может быть виден извне.
        """
    }
}