package dev.itsu.mynote.util

import com.sun.javafx.css.converters.ColorConverter
import javafx.beans.property.ObjectProperty
import javafx.beans.value.WritableValue
import javafx.css.CssMetaData
import javafx.css.Styleable
import javafx.css.StyleableObjectProperty
import javafx.css.StyleableProperty
import javafx.scene.Parent
import javafx.scene.paint.Color
import java.util.*


class CssColorHelper : Parent() {

    private var namedColor: ObjectProperty<Color>? = null

    fun namedColorProperty(): ObjectProperty<Color> {
        if (namedColor == null) {
            namedColor = object : StyleableObjectProperty<Color>(DEFAULT_NAMED_COLOR) {
                override fun getCssMetaData(): CssMetaData<out Styleable, Color> {
                    return StyleableProperties.NAMED_COLOR
                }

                override fun getBean(): Any {
                    return this@CssColorHelper
                }

                override fun getName(): String {
                    return "namedColor"
                }
            }
        }
        return namedColor!!
    }

    private fun getNamedColor(): Color {
        return namedColorProperty().get()
    }

    private object StyleableProperties {
        val NAMED_COLOR: CssMetaData<CssColorHelper, Color> = object : CssMetaData<CssColorHelper, Color>(
            "-named-color", ColorConverter.getInstance(),
            DEFAULT_NAMED_COLOR
        ) {
            override fun isSettable(n: CssColorHelper): Boolean {
                return n.namedColor == null || !n.namedColor!!.isBound
            }

            override fun getStyleableProperty(n: CssColorHelper): StyleableProperty<Color> {
                return n.namedColorProperty() as StyleableProperty<Color>
            }
        }
        var STYLEABLES: List<CssMetaData<out Styleable?, *>>? = null

        init {
            val styleables: MutableList<CssMetaData<out Styleable?, *>> = ArrayList(getClassCssMetaData())
            styleables.add(NAMED_COLOR)
            STYLEABLES = Collections.unmodifiableList(styleables)
        }
    }

    override fun getCssMetaData(): List<CssMetaData<out Styleable?, *>>? {
        return StyleableProperties.STYLEABLES
    }

    fun getNamedColor(name: String): Color {
        this.style = "-named-color: $name;"
        this.applyCss()
        return getNamedColor()
    }

    companion object {
        val DEFAULT_NAMED_COLOR: Color? = null
    }

    init {
        isFocusTraversable = false
        styleClass.add("css-color-helper")
    }
}