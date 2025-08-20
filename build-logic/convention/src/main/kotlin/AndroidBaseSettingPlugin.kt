import com.configureBaseAppVersion
import com.configureBaseSetting
import com.configureBuildType
import com.configureDaggerHilt
import com.configureUnitTestUtil
import com.getBasePluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidBaseSettingPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      getBasePluginExtension()?.let { extension ->
        configureBaseAppVersion(extension)
        configureBaseSetting(extension)
        configureBuildType(extension)
        configureDaggerHilt(extension)
        configureUnitTestUtil(extension)
      }
    }
  }
}