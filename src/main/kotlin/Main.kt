import kotlinx.serialization.Serializable

data class Usuario(val codigo: String, val nombre: String, val correo: String)

@Serializable
data class Registro(val codigo: String, var isConectado: Boolean, var ultimaConexion: String)

fun main() {

    val files = FileManager()

    val usuarios = mutableSetOf<Usuario>()
    var codigo: String

    files.cargarUsuarios(usuarios)

    while (true) {
        print("Escanea tu codigo: ")
        codigo = readln()
        clear()

        if (!usuarios.any { it.codigo == codigo }) { // si no existe lo creo
            val newUsuario = crearUsuario(codigo)
            usuarios.add(newUsuario)
            files.guardarUsuario(newUsuario)
            println("Actualmente estas desconectado, vuelve a pasar tu codigo si quieres empezar a inputar")

        } else if (!files.isConectado(codigo)) {
            val usuario = usuarios.find { it.codigo == codigo }!!

            println("Bienvenid@ ${usuario.nombre}!")
//            println("Hoy has trabajado en total ${} hasta ahora")
            files.switchConectado(codigo)
            files.actualizarRegistro()

        } else {
            val usuario = usuarios.find { it.codigo == codigo }!!

            files.addTiempo(usuario)
            files.switchConectado(codigo)
            files.actualizarRegistro()

            println("Hasta luego ${usuario.nombre}!")
            println("Hoy has trabajado en total ${files.getTotalTime(codigo)} hasta ahora")
        }
    }
}

fun crearUsuario(codigo: String): Usuario {
    while (true) {
        println("No estas registrado en el sistema.")
        print("Indica tu nombre completo: ")
        val nombre = readln()
        print("Indica tu correo electronico: ")
        val correo = readln()
        clear()
        println(" *** La informacion es correcta?")
        println("     Nombre completo: $nombre")
        println("     Correo electronico: $correo")
        println()
        print(" [y/n] : ")
        clear()
        if (readln() == "y") return Usuario(codigo,nombre,correo)
    }
}

fun clear() = print("\u001b[H\u001b[2J")