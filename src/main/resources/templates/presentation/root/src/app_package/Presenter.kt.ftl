package ${packageName};

import javax.inject.Inject

class ${CLASS_NAME}Presenter @Inject constructor(
    private val view: ${CLASS_NAME}Contract.View,
) : ${CLASS_NAME}Contract.Presenter {

    init {
        view.setPresenter(this)
    }

    override fun start() {
        TODO("not implemented")
    }

    override fun stop() {
        TODO("not implemented")
    }
}