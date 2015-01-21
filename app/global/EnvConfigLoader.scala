package global

import java.io.File

import com.typesafe.config.ConfigFactory
import play.api.{Mode, Configuration, GlobalSettings}

trait EnvConfigLoader extends GlobalSettings {

  abstract override def onLoadConfig(config: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
    val playEnv = config.getString("application.env") getOrElse mode.toString
    val modeSpecificConfig = config ++ Configuration(ConfigFactory.load(s"application.$playEnv.conf"))
    super.onLoadConfig(modeSpecificConfig, path, classloader, mode)
  }
}