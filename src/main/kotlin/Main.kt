
data class Usuario(val codigo: String, val nombre: String, val correo: String)

fun main() {

    val files = FileManager()

    val usuarios = mutableSetOf<Usuario>()
    var codigo: String

    files.cargarUsuarios(usuarios)

    while (true) {
        print("Escanea tu codigo: ")
        codigo = readln()
        clear()

        if (!usuarios.any { it.codigo == codigo }) {
            val newUsuario = crearUsuario(codigo)
            usuarios.add(newUsuario)
            files.guardarUsuario(newUsuario)
            println("Actualmente estas desconectado, vuelve a pasar tu codigo si quieres empezar a inputar")
        }

//        if (files.)
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