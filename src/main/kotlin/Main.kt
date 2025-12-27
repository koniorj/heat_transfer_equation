import org.jetbrains.kotlinx.kandy.dsl.plot
import org.jetbrains.kotlinx.kandy.letsplot.layers.line
import org.jetbrains.kotlinx.kandy.letsplot.export.save
import org.jetbrains.kotlinx.kandy.letsplot.layers.points

fun main() {
    print("Prosze o podanie n (liczba calkowita): ")
    val input = readln().toInt()
    val n = maxOf(3, input)

    val geometry = Geometry(n, 3.0)
    val solver = Solver(geometry)

    solver.buildMatrix()
    val results = solver.gaussianElimination().toList() // mamy rozklad temperatur. Pozostalo zwizualizowac
    val coordinates = geometry.nodesArray.toList()

    val chart = mapOf(
        "x" to coordinates,
        "u(x)" to results
    )

    chart.plot {
        line {
            x("x")
            y("u(x)")
        }
        if (n < 30) {
            points {
                x("x")
                y("u(x)")
            }
        }
    }.save("wykres_dla_$n.png")
}