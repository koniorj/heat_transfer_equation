class Geometry(val n: Int, val length: Double) {
    val h: Double = length/n // szerokosc 1 elem

    // lista na wspolrzedne wezlow:
    val nodesArray: DoubleArray = DoubleArray(n+1) {i -> i*h}

    // dla danego materialu
    fun getK(x: Double): Double { // pret sklada sie z dwoch materialow
        return if (x > 1) 1.0 else 0.5
    }
}