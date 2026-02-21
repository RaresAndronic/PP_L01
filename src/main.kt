import java.util.*

class Nod(val valoare: String) {
    var stanga : Nod? = null
    var dreapta : Nod? = null
}

fun CreareLista(sir : String) : MutableList<String> {
    val lista = mutableListOf<String>()
    var nr = ""
    for(car in sir) {
        if(car.isDigit() || car == '.') {
            nr += car
        }
        else {
            if(nr.isNotEmpty()) {
                lista.add(nr)
                nr = ""
            }
            lista.add(car.toString())
        }
    }
    if(nr.isNotEmpty()) {
        lista.add(nr)
    }
    return  lista
}

fun CreareArbore(lista : MutableList<String>) : Nod? {
    if(lista.size == 0) {
        return null
    }
    if(lista.size == 1) {
        return Nod(lista[0])
    }
    if(ParantezeStgDR(lista)) {
        return CreareArbore(lista.subList(1, lista.size - 1))
    }
    var paranteza = 0
    var index = 0
    var prioritareMin = 3
    var prioritateCurenta = 0
    for (i in lista.indices) {
        if(lista[i] == "(") {
            paranteza++
        }
        else if(lista[i] == ")") {
            paranteza--
        }
        else if (paranteza == 0) {
            prioritateCurenta = when (lista[i]) {
                "+", "-" -> 1
                "*", "/" -> 2
                else -> 3
            }
            if (prioritateCurenta <= prioritareMin) {
                prioritareMin = prioritateCurenta
                index = i
            }
        }
    }
    val radacina = Nod(lista[index])
    radacina.stanga = CreareArbore(lista.subList(0, index))
    radacina.dreapta = CreareArbore(lista.subList(index + 1, lista.size))
    return  radacina
}

fun ParantezeStgDR(lista : MutableList<String>) : Boolean {
    if(lista.isEmpty() || lista.first() != "(" || lista.last() != ")") {
        return false
    }
    var paranteza = 0
    val stop = lista.size - 1
    var i = 0
    while(i < stop - 1) {
        if(lista[i] == "(") {
            paranteza++
        }
        else if(lista[i] == ")") {
            paranteza--
        }
        else if (paranteza == 0) {
            return false
        }
        i++
    }
    return true
}

fun PostOrdine(rad : Nod?) {
    if(rad != null) {
        PostOrdine(rad.stanga)
        PostOrdine(rad.dreapta)
        print(rad.valoare)
    }
}

fun CalcExpPostfixata(rad : Nod?) : Double {
    if(rad == null) {
        return 0.0
    }
    if (rad.stanga == null && rad.dreapta == null) {
        return rad.valoare.toDouble()
    }
    val valoareStanga = CalcExpPostfixata(rad.stanga)
    val valoareDreapta = CalcExpPostfixata(rad.dreapta)
    return when (rad.valoare) {
        "+" -> valoareStanga + valoareDreapta
        "-" -> valoareStanga - valoareDreapta
        "*" -> valoareStanga * valoareDreapta
        "/" -> valoareStanga / valoareDreapta
        else -> {
            print("Operator invalid!")
            0.0
        }
    }
}

fun main(args: Array<String>) {
    val reader = Scanner(System.`in`)
    print("Scrie o expresie: ")
    val a = reader.nextLine();
    val exp_infixata = CreareLista(a)
    val arbore = CreareArbore(exp_infixata)
    print("expresia postfixata este ")
    PostOrdine(arbore)
    print("\nrez expresiei este ")
    print(CalcExpPostfixata(arbore))
}
