import com.android.build.api.dsl.ApplicationExtension
import com.configureAndroidCompose
import com.configureUiUtil
import com.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.impldep.org.apache.commons.lang.ArrayUtils.add
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposePlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
            configureUiUtil(extension)
        }
    }
}