import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Properties

class FileManager {

    private val properties = Properties()
    private val defaultProperties = ClassLoader.getSystemResourceAsStream("config.properties")

    private val internalDir = File(System.getProperty("user.home"),".workTimeScanner")

    private val lastTimeExec = File(internalDir,"config.properties")
    val registry = File(internalDir,"registry.json")
    val userInfo = File("userInfo.csv")
    private val data = File("data.csv")

    init {
        // crear los archivos y carpetas que no haya
        if (!internalDir.exists()) internalDir.mkdirs()
        if (!lastTimeExec.exists()) properties.load(defaultProperties)
        else FileInputStream(lastTimeExec).use { properties.load(it) }
        if (!data.exists()) data.writeText("Usuario,Fecha,Tiempo")
        if (!userInfo.exists()) userInfo.writeText("")

        // si es el dia siguiente
        if (LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE).toInt()
            != properties.getProperty("lastTimeExec").toInt()) {

            registry.writeText("") // resetea el registro

            // guardar nuevo lastTimeExec
            properties.setProperty("lastTimeExec",LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE).toString())
            FileOutputStream(lastTimeExec).use { properties.store(it, null) }
        }
    }

    fun guardarUsuario(usuario: Usuario) {
        userInfo.appendText("${usuario.codigo},${usuario.nombre},${usuario.correo}")
    }

    fun cargarUsuarios(usuarios: MutableSet<Usuario>) {
        for (usuario in userInfo.readLines()) {
            val datos = usuario.split(",")
            usuarios.add(Usuario(datos[0],datos[1],datos[2]))
        }
    }

//    fun getTrabajado(codigo: Int): LocalTime {
//
//    }

}