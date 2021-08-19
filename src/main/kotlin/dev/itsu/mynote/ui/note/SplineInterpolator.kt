package dev.itsu.mynote.ui.note

/*
 * Copyright (C) 2012 Makoto Yamazaki <zaki@uphyca.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.math3.linear.*


/**
 * http://www.akita-nct.ac.jp/yamamoto/lecture/2004/5E/interpolation/text/html/node3.html 読んで書いた
 * http://commons.apache.org/math から commons-math3-3.0 を持ってきてクラスパスを通してください。
 *
 * 3次スプライン補間なので、N + 1 個の (x, y) 座標の組から N 個の3次関数を作成して補完します。
 * また、与えられた座標において、隣接する3次関数の1次導関数と2次導関数が等しくなります。
 */
class SplineInterpolator @JvmOverloads constructor(
    val xCoordinates: DoubleArray,
    val yCoordinates: DoubleArray,
    length: Int = xCoordinates.size
) {
    /**
     * 座標列によって区切られる区間(3次関数)の数
     */
    private val N: Int

    /**
     * X 座標列
     */
    private val mXCoordinates: DoubleArray

    /**
     * Y 座標列
     */
    private val mYCoordinates: DoubleArray
    /*
     * 補間のための3次関数群の係数情報
     *
     * 使用する関数のインデックスを j とすると、 0 <= j < N で、
     * y = mA[j](x-x[j])^3 + mB[j](x-x[j])^2 + mC[j](x-x[j])^1 + mD[j]
     */
    /**
     * 3次の項に対する係数列。長さ N。
     */
    private val mA: DoubleArray

    /**
     * 2次の項に対する係数列。長さ N。
     */
    private val mB: DoubleArray

    /**
     * 1次の項に対する係数列。長さ N。
     */
    private val mC: DoubleArray

    /**
     * 0次の項に対する係数列。長さ N。
     */
    private val mD: DoubleArray

    /**
     * 与えられた `x` に対する y の値を返します。
     *
     * @param x x値。
     * @return (補間によって計算された) y の値。
     */
    operator fun get(x: Double): Double {
        val xCoordinates = mXCoordinates
        // FIXME 区間がたくさんある場合は NavigableMap とか使った方がいい
        var targetFuntionIndex = N - 1 // 見つからない場合は最後のものを使う
        for (j in 0 until N) {
            if (x <= xCoordinates[j + 1]) {
                targetFuntionIndex = j
                break
            }
        }

        // (x - x_j)^1
        val x1 = x - xCoordinates[targetFuntionIndex]
        // (x - x_j)^2
        val x2 = x1 * x1
        // (x - x_j)^3
        val x3 = x2 * x1
        return mA[targetFuntionIndex] * x3 + mB[targetFuntionIndex] * x2 + mC[targetFuntionIndex] * x1 + mD[targetFuntionIndex]
    }

    /**
     * 補間に必要な値を計算します。
     */
    private fun calculate() {
        val xCoordinates = mXCoordinates
        val yCoordinates = mYCoordinates
        val h = DoubleArray(N)
        for (j in 0 until N) {
            h[j] = xCoordinates[j + 1] - xCoordinates[j]
        }
        val coefficients: RealMatrix = buildCoefficients(h)
        val constants: RealVector = buildConstants(h)

        // 行列式を解いて u[1] から u[N - 1] までを求める。
        val solver: DecompositionSolver = LUDecomposition(coefficients).solver
        // u の index はずれているので、値を取り出す時は getUAt(int) を使うこと
        val u: RealVector = solver.solve(constants)

        // mA, mB, mC, mD の値を求める
        var j = 0
        val length = N
        while (j < length) {
            val u_j = getUAt(u, j)
            val u_j1 = getUAt(u, j + 1)
            val y_j = yCoordinates[j]
            val y_j1 = yCoordinates[j + 1]
            mA[j] = (u_j1 - u_j) / (6.0 * h[j])
            mB[j] = u_j / 2.0
            mC[j] = (y_j1 - y_j) / h[j] - h[j] * (2.0 * u_j + u_j1) / 6.0
            mD[j] = y_j
            j++
        }
    }

    /**
     * 補間関数の係数を求める際に使用する u[] を計算する際の行列式の係数行列(AU = B の A)
     * を構築します。
     *
     * @param h `x[j + 1] - x[j]`の配列
     * @return 係数行列。
     */
    private fun buildCoefficients(h: DoubleArray): RealMatrix {
        val coefficients: RealMatrix = Array2DRowRealMatrix(N - 1, N - 1)
        for (rowIndex in 0 until N - 1) {
            val targetH = h[rowIndex]
            val nextH = h[rowIndex + 1]
            if (rowIndex != 0) {
                coefficients.setEntry(rowIndex, rowIndex - 1, targetH)
            }
            coefficients.setEntry(rowIndex, rowIndex, 2.0 * (targetH + nextH))
            if (rowIndex != N - 2) {
                coefficients.setEntry(rowIndex, rowIndex + 1, nextH)
            }
        }
        return coefficients
    }

    /**
     * 補間関数の係数を求める際に使用する u[] を計算する際の行列式の定数列(AU = B の B)
     * を構築します。
     *
     * @param h `x[j + 1] - x[j]`の配列
     * @return 定数列。
     */
    private fun buildConstants(h: DoubleArray): RealVector {
        val yCoordinates = mYCoordinates
        val v = DoubleArray(N) // v[0] は使わない
        var pv = (yCoordinates[1] - yCoordinates[0]) / h[0]
        for (j in 1 until N) {
            val temp = (yCoordinates[j + 1] - yCoordinates[j]) / h[j]
            v[j] = 6.0 * (temp - pv)
            pv = temp
        }
        val constants: RealVector = ArrayRealVector(N - 1)
        for (j in 1 until N) {
            constants.setEntry(j - 1, v[j])
        }
        return constants
    }

    companion object {
        private fun ensureInputsAreValid(
            xCoordinates: DoubleArray, yCoordinates: DoubleArray,
            length: Int
        ) {
            require(length >= 2) { "'length' must be 2 or more." }
            require(xCoordinates.size >= length) { "length of 'xCoordinates' must not be less than 'length'" }
            require(yCoordinates.size >= length) { "length of 'yCoordinates' must not be less than 'length'" }

            // x が単調増加であることと、座標にNaN や無限大が含まれないことを確認
            val prevX = Double.NEGATIVE_INFINITY
            for (i in 0 until length) {
                val x = xCoordinates[i]
                val y = yCoordinates[i]
                require(!(x.isNaN() || x.isInfinite())) { "'xCoodinates' must not contain NaN nor INFINITY" }
                require(!(y.isNaN() || y.isInfinite())) { "'yCoodinates' must not contain NaN nor INFINITY" }
                require(x > prevX) { "elements in 'xCoodinates' must monotonically increase" }
            }
        }

        /**
         * `` u が保持する値のインデックスは、他の計算部分に使用するものとズレが
         * あるので、ズレを吸収してアクセスするためのユーティリティメソッドです。
         * u に含まれていない値については、 natural spline になるように補います。
         *
         * @param u u[1] から u[N - 1] の N - 1 個分の u値を保持する [RealVector]。
         * @param j 計算式上での u のインデックス。
         * @return 計算式上での u[j] の値。
         */
        private fun getUAt(u: RealVector, j: Int): Double {
            return if (j == 0 || u.dimension < j) {
                // natural spline なので 0。
                0.0
            } else u.getEntry(j - 1)
        }
    }

    init {
        ensureInputsAreValid(xCoordinates, yCoordinates, length)
        N = length - 1
        mXCoordinates = DoubleArray(N + 1)
        System.arraycopy(xCoordinates, 0, mXCoordinates, 0, length)
        mYCoordinates = DoubleArray(N + 1)
        System.arraycopy(yCoordinates, 0, mYCoordinates, 0, length)
        mA = DoubleArray(N)
        mB = DoubleArray(N)
        mC = DoubleArray(N)
        mD = DoubleArray(N)

        // fill mA, mB, mC, mD
        calculate()
    }
}