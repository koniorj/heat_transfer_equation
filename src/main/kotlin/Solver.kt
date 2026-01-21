import kotlin.math.sqrt

class Solver(val geometry: Geometry) {
    private val size = geometry.n + 1
    private val matrixK = Array(size) { DoubleArray(size) }
    private val vectorF = DoubleArray(size)

    // wektor u - szukane -> temp w kazdym pkcie

    // chcemy pkty Gaussa i wagi
    private val gaussWeights = doubleArrayOf(1.0, 1.0)
    private val pointValue: Double =  1.0 / sqrt(3.0)
    private val gaussPoints = doubleArrayOf(-pointValue, pointValue)

     // K * u = F, szukamy u

    fun buildMatrix() {
        for (element in 0 until geometry.n) {
            for (i in 0 until 2) {
                val gaussPoint = gaussPoints[i]
                val weight = gaussWeights[i]

                // gdzie jestesmy? z zakresu -1,1 -> 0,3 wymiary preta
                val realPoint = (gaussPoint + 1.0) * 0.5 * geometry.h + geometry.nodesArray[element]
                //jaki jest material?
                val k = geometry.getK(realPoint)
                // zeby dopasowac wynik do realnej szerokosci. Jakobian
                val scale = geometry.h / 2.0

                // funkcja ksztaltu - jak temp rozchodzi sie miedzy naszymi wezlami
                // umowilismy sie ze temp przeplywa liniowo, f ksztaltu sa liniowe,
                // wiec ich pochodne dPhi sa stale
                val dPhiRight = 1.0 / geometry.h // zamiast u' i v'. Nachylenie
                val dPhiLeft = -1.0 / geometry.h
                // moglibysmy uzyc 1 punktu (f podcalkowa jest stala), ale
                // k jest zmienne! wtedy moglibysmy nie zlapac momentu zmiany

                // zal. u(x) = u0 * dPhi0(x) + u1 * dPhi1(X) + ...
                // k * dphiLeft * dphiRight -> ile ciepla przeplynie z lewej na prawa przez material k

                // calka mowi nam o zdolnosci preta do transportu ciepla w danym pkcie. ku'v'
                // duza calka -> dobra zdolnosc

                // jako funkcje testujace v wybieramy dokladnie
                // te same nachylenia, ktorych uzylismy do opisu temp u
                // dla kazdego elementu dostajemy 4 liczby:

                // w matrixK przechowujemy wsp stojace przy niewiadomych ui
                // np. dPhiLeft * dPhiRight jak stromosc lewego wplywa na stromosc prawego
                matrixK[element][element] += scale * k * dPhiLeft * dPhiLeft * weight
                matrixK[element][element+1] += scale * k * dPhiLeft * dPhiRight * weight
                matrixK[element+1][element] += scale * k * dPhiRight * dPhiLeft * weight
                matrixK[element+1][element+1] += scale * k * dPhiRight * dPhiRight * weight
            }
        }

        // calka + 1/2u(0)v(0) = -1/2v(0)

        matrixK[0][0] += 0.5
        vectorF[0] = -0.5 // vectorF ma wyplenione 2 pola, bo f(x) (f. zrodla) = 0. F = calka z f(x)v(x)
        vectorF[size-1] = 3.0 // u(3)=3

        for (j in 0 until size) { // wiemy z polecenia
            matrixK[size-1][j] = 0.0
        }

        matrixK[size-1][size-1] = 1.0  // 1 * un = 3

    }

    // eliminacja gaussa - inspirowane algorytmem z geeksforgeeks
    fun gaussianElimination(): DoubleArray {
        val tempMatrix = Array(size) { i -> matrixK[i].copyOf() }
        val tempVector = vectorF.copyOf()

        // nie mamy zer na przekatnej. nie musimy robic rowswapow
        // ogolnie bardzo upraszcza sie nam sprawa eliminacji gaussa przez
        // nature matrixK -> wezly oddzialuja tylko ze swoimi sasiadami i niezerowe
        // wartosci znajduja sie praktycznie tylko wokol przekatnej

        for (k in 0 until size) {
            for (i in k+1 until size) {
                val factor = tempMatrix[i][k] / tempMatrix[k][k]
                tempVector[i] -= factor * tempVector[k]

                for (j in k until size) {
                    tempMatrix[i][j] -= factor * tempMatrix[k][j]
                }
            }
        }

        // back substitution
        // po eliminacji ostatni wezel zalezy tylko od siebie
        val resultU = DoubleArray(size)
        for (i in size-1 downTo 0) {
            var sum = 0.0

            for (j in i+1 until size) {
                sum += tempMatrix[i][j] * resultU[j]
            }
            resultU[i] = (tempVector[i] - sum) / tempMatrix[i][i]
        }
        return resultU
    }
}


//∫ k(x)·(du/dx)·(dv/dx) dx
// u(x) przyblizamy jako: u(x) = u₁·N₁(x) + u₂·N₂(x) funkcje ksztaltu N
//N₁(x) = 1 - x   (liniowo maleje od 1 do 0)
//N₂(x) = x       (liniowo rośnie od 0 do 1)
// du/dx = u₁·dN₁/dx + u₂·dN₂/dx = u₁·(-1/h) + u₂·(1/h)