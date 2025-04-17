import com.configureAndroidCompose
import com.configureAndroidTestUtil
import com.configureUiUtil
import com.getBasePluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidUiPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      getBasePluginExtension()?.let { extension ->
        configureAndroidCompose(extension)
        configureUiUtil(extension)
        configureAndroidTestUtil(extension)
      }
    }
  }
}