package ${packageName};

import com.app.dealfish.di.AutoInject
import javax.inject.Inject

@AutoInject
open class ${CLASS_NAME}Fragment @Inject constructor() : Fragment(), ${CLASS_NAME}Contract.View {

    @Inject
    lateinit var myPresenter: ${CLASS_NAME}Contract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.${LAYOUT_RES_ID}, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TODO("not implemented")
    }

    override fun setPresenter(presenter: ${CLASS_NAME}Contract.Presenter) {
        myPresenter = presenter
    }

    override fun onStart() {
        super.onStart()
        myPresenter.start()
    }

    override fun onStop() {
        super.onStop()
        myPresenter.stop()
    }

     companion object {

        fun newInstance(): ${CLASS_NAME}Fragment {
            val fragment = ${CLASS_NAME}Fragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }

     }
}