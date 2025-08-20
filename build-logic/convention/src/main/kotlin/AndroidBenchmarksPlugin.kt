import com.configureBaseAppVersion
import com.configureBenchmarks
import com.getBasePluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidBenchmarksPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      getBasePluginExtension()?.let { extension ->
        configureBaseAppVersion(extension)
        configureBenchmarks(extension)
      }
    }
  }
}