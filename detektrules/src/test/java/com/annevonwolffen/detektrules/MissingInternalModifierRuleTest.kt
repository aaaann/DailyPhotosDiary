package com.annevonwolffen.detektrules

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
        """.trimIndent()

        assertThat(MissingInternalModifierRule().compileAndLint(code))
            .hasSize(1)
    }
}