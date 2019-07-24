package ge.edu.freeuni.chatuna.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import ge.edu.freeuni.chatuna.R

class CustomToolbar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private var headerText: String?
    private var showBackButton: Boolean
    private var showMenuButton: Boolean
    private var showDeleteButton: Boolean

    interface OnMainItemsClickListener {
        fun onMenuClick()
    }

    interface OnSecondaryItemsClickListener {
        fun onBackClick()
        fun onDeleteClick()
    }

    private var onMainItemsClickListener: OnMainItemsClickListener? = null
    private var onSecondaryItemsClickListener: OnSecondaryItemsClickListener? = null

    @BindView(R.id.img_delete)
    lateinit var imgDelete: ImageView

    @BindView(R.id.img_back)
    lateinit var imgBack: ImageView

    @BindView(R.id.img_menu)
    lateinit var imgMenu: ImageView

    @BindView(R.id.tv_header)
    lateinit var tvHeader: TextView

    @OnClick(R.id.img_delete)
    fun onDeleteClick() {
        onSecondaryItemsClickListener?.onDeleteClick()
    }

    @OnClick(R.id.img_back)
    fun onBackClick() {
        onSecondaryItemsClickListener?.onBackClick()
    }

    @OnClick(R.id.img_menu)
    fun onMenuClick() {
        onMainItemsClickListener?.onMenuClick()
    }

    init {
        inflate(getContext(), R.layout.custom_toolbar, this)
        ButterKnife.bind(this)

        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomToolbar, 0, 0).apply {
            try {
                showDeleteButton = getBoolean(R.styleable.CustomToolbar_show_delete_button, false)
                showMenuButton = getBoolean(R.styleable.CustomToolbar_show_menu_button, false)
                showBackButton = getBoolean(R.styleable.CustomToolbar_show_back_button, false)
                headerText = getString(R.styleable.CustomToolbar_header_text)
            } finally {
                recycle()
            }
        }
        initView()
    }

    private fun initView() {
        imgDelete.visibility = if (showDeleteButton) View.VISIBLE else View.GONE
        imgBack.visibility = if (showBackButton) View.VISIBLE else View.GONE
        imgMenu.visibility = if (showMenuButton) View.VISIBLE else View.GONE
        tvHeader.text = headerText
    }

    fun setHeader(headerText: String) {
        tvHeader.text = headerText
    }

    fun setListener(onMainItemsClickListener: OnMainItemsClickListener) {
        this.onMainItemsClickListener = onMainItemsClickListener
    }

    fun setListener(onSecondaryItemsClickListener: OnSecondaryItemsClickListener) {
        this.onSecondaryItemsClickListener = onSecondaryItemsClickListener
    }
}