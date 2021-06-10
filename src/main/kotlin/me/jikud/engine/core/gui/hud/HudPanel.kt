package me.jikud.engine.core.gui.hud

import me.jikud.engine.core.helpers.PPoint
import kotlin.math.max

class HudPanel(width: Int = 0, height: Int = 0, var alignment: Alignment = Alignment.NONE) :
    HudComponent(width, height) {
    constructor(alignment: Alignment) : this(width = 0, height = 0, alignment)

    val components = ArrayList<HudComponent>()

    init {
        showBackground = true
        componentUILevel++
    }

    override fun draw() {
        super.draw()
        components.forEach { it.draw() }
    }

    override fun postUpdate() {
        super.postUpdate()
        components.forEach { it.postUpdate() }
    }

//    fun updatePanelPos(pos: PPoint) {
//        for (c in components) {
//            c.pos = PPoint(c.pos).translate(pos)
//        }
//    }

    fun pack() {
        updatePanelComponents(this)
    }

    //    fun addComponent(c: HudComponent) = addComponent(c, false)
    fun addComponent(c: HudComponent) {
        if (this == c) throw Error("wtf chel, you adding yourself to component list")
//        c.parent = this
        when (alignment) {
            Alignment.HORIZONTAL -> {
                c.pos = PPoint(width * 1f, c.pos.y)
                width = max(width, width + c.width)
                height = max(height, c.height)
            }
            Alignment.VERTICAL -> {
                c.pos = PPoint(c.pos.x, height * 1f)
                width = max(width, c.width)
                height = max(height, height + c.height)
            }
            else -> {}
        }
        components.add(c)
    }

    //todo: пофиксить позицию компонентов панели при паковке одного компонента как внутренняя панель с добавлением отдельного компонента рядом
    private fun updatePanelComponents(c: HudPanel) {
        for (h in c.components) {
            h.pos = PPoint(h.pos).translate(c.pos.x, c.pos.y)
            if (h is HudPanel) {
                updatePanelComponents(h)
            }
        }
    }

    fun removeComponent(c: HudComponent) {
        if (components.contains(c))
            components.remove(c)
    }

    override fun update() {
        super.update()
        components.forEach(HudComponent::update)
    }
}