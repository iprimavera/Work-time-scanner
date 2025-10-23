import java.io.File
import java.time.LocalDate

fun main() {

    val lastTimeExec = File(".lastTimeExec.txt") // properties mejor
    val userInfo = File("userInfo.csv")
    val data = File("data.csv")
    val registry = File("registry.json")
    println(LocalDate.now().isAfter(LocalDate.of(2025,10,31)))

    while (true) {
        print("Escanea tu codigo: ")
        var codigo = readln()
        clear()
    }
}

fun clear() = print("\u001b[H\u001b[2J")