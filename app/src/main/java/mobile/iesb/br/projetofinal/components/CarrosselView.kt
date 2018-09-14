package mobile.iesb.br.projetofinal.components

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import mobile.iesb.br.projetofinal.R

class CarrosselView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    val tempoCarrossel: Long = 3000
    var imagens = intArrayOf(R.drawable.noticias_logo, R.drawable.noticia_um, R.drawable.noticia_dois)
    var contador: Int = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var paint = Paint()
        val bmp = BitmapFactory.decodeResource(resources, imagens[contador])
        val matrix = Matrix()

        var escalaX: Float = width.toFloat() / bmp.width.toFloat()
        var escalaY: Float = height.toFloat() / bmp.height.toFloat()

        matrix.postScale(escalaX, escalaY)
        val dstbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
        canvas.drawColor(Color.TRANSPARENT)

        canvas.drawBitmap(dstbmp, 0f, 0f, paint)

        postInvalidateDelayed(tempoCarrossel)


        contador++
        if (contador >= imagens.size)
            contador = 0
    }
}
