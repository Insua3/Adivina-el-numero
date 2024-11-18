import java.io.File

fun main() {

    while (true) {
        println("1. Jugar")
        println("2. Ver la ultima jugada")
        println("3. Salir")

        when (readln()) {
            "1" -> jugarnuevapartida()
            "2" -> verultimajugada()
            "3" -> {
                break
            }

            else -> println("Escoje 1, 2 o 3")
        }
    }

}

fun jugarnuevapartida() {
    println("Adivina el numero, tienes 10 intentos")

    val longitudnumero = 4
    val numeromaxintentos = 10
    val numerosecreto = generarnumerosecreto(4)

    var intentos = 1
    var acertado = false

    while (intentos < numeromaxintentos && !acertado) {
        print("Intento numero $intentos: Ingresa un numero de 4 digitos:")
        val intentousuario = readln().trim() ?: ""


        if (!numerovalido(intentousuario, longitudnumero)) {
            println("Ingresa un numero de 4 digitos")
            continue
        }

        val resultado = calcularcoincidencias(numerosecreto, intentousuario)
        val aciertos = resultado.first
        val coincidencias = resultado.second

        println("Número ingresado: $intentousuario -> Aciertos: $aciertos , Coincidencias: $coincidencias")

        if (aciertos == longitudnumero) {
            acertado = true
        } else {
            intentos++
        }
    }
    if (acertado) {
        println("Felicidades, has acertado el numero")
    } else {
        println("No has adivinado, el numero era: $numerosecreto")
    }
    guardarjugada(numerosecreto, intentos)
}

fun verultimajugada() {
    val txtultimajugada = File("jugada.txt")
    if (txtultimajugada.exists()) {
        val ultimajugada = txtultimajugada.readText()
        println("Ultima partida jugada")
        println(ultimajugada)
    } else {
        println("No hay ninguna jugada guardada")
    }
}


fun generarnumerosecreto(longitud: Int): String {
    val numeros = (1..6).shuffled().take(longitud)
    val numerosecreto = numeros.joinToString("")
    return numerosecreto
}

fun numerovalido(numero: String, longitud: Int): Boolean {
    if (numero.length != longitud) {
        return false
    }
    if (!numero.all { it.isDigit() }) {
        return false
    }
    if (numero.toSet().size != longitud) {
        return false
    }
    return true
}

fun calcularcoincidencias(numerosecreto: String, numerointroducido: String): Pair<Int, Int> {
    var aciertos = 0
    var coincidencias = 0

    if (numerosecreto.length != numerointroducido.length) {
        return Pair(0, 0)
    }

    val listanumerosecreto = numerosecreto.toMutableList()
    val listanumerointroducido = numerointroducido.toMutableList()

    for (i in numerosecreto.indices) {
        if (numerosecreto[i] == numerointroducido[i]) {
            aciertos++
            listanumerosecreto[i] = '-'
            listanumerointroducido[i] = '-'
        }
    }

    for (i in numerointroducido.indices) {
        if (listanumerointroducido[i] != '-' && listanumerointroducido[i] in listanumerosecreto) {
            coincidencias++
            val posicion = listanumerosecreto.indexOf(listanumerointroducido[i])
            listanumerosecreto[posicion] = '-'
        }
    }
    return Pair(aciertos, coincidencias)
}

fun guardarjugada(numerosecreto: String, intentos: Int) {
    val txtultimajugada = File("jugada.txt")
    txtultimajugada.writeText("Numero secreto: $numerosecreto, Numero de intentos: $intentos")
}