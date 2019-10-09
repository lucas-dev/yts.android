package com.alvefard.yify.presentation.screens.listing

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.presentation.App
import com.alvefard.yify.presentation.common.Constants
import com.alvefard.yify.presentation.common.base.BaseContract
import com.alvefard.yify.presentation.common.base.BaseController
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigatorConductor
import com.alvefard.yify.presentation.common.GenericAdapter
import com.alvefard.yify.presentation.common.ux.imageloader.ImageLoader
import com.alvefard.yify.presentation.screens.common.MovieViewHolder
import com.alvefard.yify.presentation.util.BundleBuilder
import com.alvefard.yify.presentation.util.MoviesDiffUtil
import com.alvefard.yify.presentation.util.StringUtils
import com.alvefard.yifymovies.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.jakewharton.rxbinding3.recyclerview.scrollStateChanges
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.supercharge.shimmerlayout.ShimmerLayout
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ListingScreen(args: Bundle) : BaseController(args), ListingContract.View {

    @Inject
    lateinit var toolbar: ViewGroup

    @Inject
    lateinit var presenter: ListingContract.Presenter

    @Inject
    lateinit var screenNavigator: ScreenNavigatorConductor

    @Inject
    lateinit var imageLoader: ImageLoader

    @BindView(R.id.vf_movies)
    lateinit var viewContainer: ViewFlipper
    @BindView(R.id.rv_content)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.filter_bottom_sheet)
    lateinit var bottomSheetFilter: View
    @BindView(R.id.btn_filter_clear)
    lateinit var btnBottomSheetClear: Button
    @BindView(R.id.btn_filter)
    lateinit var btnBottomSheetFilter: Button
    @BindView(R.id.chipgroup_quality)
    lateinit var chipGroupFilterQuality: ChipGroup
    @BindView(R.id.chipgroup_rating)
    lateinit var chipGroupFilterRating: ChipGroup
    @BindView(R.id.chipgroup_genre)
    lateinit var chipGroupFilterGenre: ChipGroup
    @BindView(R.id.chip_quality)
    lateinit var chipFilterQuality: Chip
    @BindView(R.id.chip_rating)
    lateinit var chipFilterRating: Chip
    @BindView(R.id.chip_genre)
    lateinit var chipFilterGenre: Chip
    @BindView(R.id.switch_orderby)
    lateinit var switchFilterOrderBy: SwitchCompat

    @BindView(R.id.tv_error_msg)
    lateinit var tvErrorMsg: TextView
    @BindView(R.id.btn_retry)
    lateinit var btnErrorRetry: View

    @BindView(R.id.load_more_indicator)
    lateinit var viewLoadMore: View
    @BindView(R.id.tv_loading_indicator_message)
    lateinit var tvLoadMore: TextView
    @BindView(R.id.vf_loading_indicator)
    lateinit var vfLoadingIndicator: ViewFlipper

    @BindView(R.id.shimmer)
    lateinit var viewShimmer: ShimmerLayout

    private lateinit var movieType: String

    private lateinit var vsToolbarIcon: ViewSwitcher

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private lateinit var adapter: GenericAdapter<Movie>

    constructor(type: String) : this(BundleBuilder(Bundle()).putString(TYPE, type).build())

    init {
        retainViewMode = RetainViewMode.RETAIN_DETACH
    }

    override fun getPresenter(): BaseContract.Presenter<*> {
        return presenter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_movies
    }

    override fun injectDependencies() {
        App.moviesPagerComponent.inject(this)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        screenNavigator.unlockSideMenu()
    }

    override fun onCreateView() {
        presenter.attachView(this)

        movieType = args.getString(TYPE)

        setUpRecyclerView()
        setUpBottomSheet()

        presenter.start()
    }

    private fun setUpRecyclerView() {
        recyclerView.setHasFixedSize(true)
        val layout = GridLayoutManager(activity, 2)
        recyclerView.layoutManager = layout

        adapter = object : GenericAdapter<Movie>() {

            override fun setViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
                return MovieViewHolder(view)
            }

            override fun onBindData(holder: RecyclerView.ViewHolder, `val`: Movie) {
                with(holder as MovieViewHolder) {
                    tvTitle.text = `val`.title
                    tvRating.text = `val`.rating.toString()
                    tvRuntime.text = StringUtils.getFormattedRuntime(`val`.runtime)
                    tvYear.text = `val`.releaseDate.toString()
                    imageLoader.loadImage(`val`.coverMedium, imageView)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imageView.transitionName = `val`.id
                    }
                }
            }

            override fun updateData(items: MutableList<Movie>) {
                val diffUtil = MoviesDiffUtil(getItems(), items)
                val diffResult = DiffUtil.calculateDiff(diffUtil)
                getItems().clear()
                getItems().addAll(items)
                diffResult.dispatchUpdatesTo(this)
            }
        }
        recyclerView.adapter = adapter
    }

    private fun setUpBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetFilter)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        val viewFlipper = toolbar.findViewById<ViewFlipper>(R.id.vs_filter)
        vsToolbarIcon = when (movieType) {
            Constants.MovieType.LATEST ->  viewFlipper.getChildAt(0) as ViewSwitcher
            Constants.MovieType.TOP_RATED ->  viewFlipper.getChildAt(1) as ViewSwitcher
            Constants.MovieType.POPULAR ->  viewFlipper.getChildAt(2) as ViewSwitcher
            else -> throw IllegalArgumentException("Illegal movie type")
        }
    }

    override fun resetFilters() {
        chipGroupFilterGenre.clearCheck()
        chipFilterGenre.isChecked = true
        chipGroupFilterQuality.clearCheck()
        chipFilterQuality.isChecked = true
        chipGroupFilterRating.clearCheck()
        chipFilterRating.isChecked = true
        switchFilterOrderBy.isChecked = false
    }


    override fun showLoadingScreen() {
        viewContainer.displayedChild = 0
        viewShimmer.startShimmerAnimation()
    }

    override fun showErrorScreen(errorMsg: String) {
        Timber.e("Error loading $movieType: $errorMsg")
        viewShimmer.stopShimmerAnimation()
        viewContainer.displayedChild = 1
        tvErrorMsg.text = errorMsg
    }

    override fun showContentScreen() {
        viewShimmer.stopShimmerAnimation()
        viewContainer.displayedChild = 2
    }

    override fun observeRetryLoadingButtonClick(): Observable<*> {
        return btnErrorRetry.clicks()
    }

    override fun observeClearFilterButton(): Observable<*> {
        return btnBottomSheetClear.clicks()
    }

    override fun observeFilterButton(): Observable<*> {
        return btnBottomSheetFilter.clicks()
    }

    override fun observeToolbarClick(): Observable<*> {
        return vsToolbarIcon.clicks()
    }

    override fun listRequestNewPage(): Observable<Boolean> {
        return recyclerView.scrollStateChanges().map {
            val isBottomReached = !recyclerView.canScrollVertically(1)
            isBottomReached && viewLoadMore.visibility == View.GONE
        }.throttleFirst(500, TimeUnit.MILLISECONDS).filter { aBoolean: Boolean -> aBoolean }
    }

    override fun observeMovieClick(): Observable<Movie> {
        return adapter.viewClickObserable
    }

    override fun expandBSFilter() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun collapseBSFilterTotal() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun collapseBSFilterPartial() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun showFilterIconNormalState() {
        vsToolbarIcon.displayedChild = 0
    }

    override fun showFilterIconWarningState() {
        vsToolbarIcon.displayedChild = 1
    }

    override fun loadFilters(quality: String, genre: String, rating: String, orderBy: String) {
        checkChipByValue(chipGroupFilterRating, rating)
        checkChipByValue(chipGroupFilterGenre, genre)
        checkChipByValue(chipGroupFilterQuality, quality)
        switchFilterOrderBy.isChecked = orderBy == "asc"
    }

    override fun getQualityFilterVal(): String {
        return getCheckedChipValue(chipGroupFilterQuality)
    }

    override fun getGenreFilterVal(): String {
        return getCheckedChipValue(chipGroupFilterGenre)
    }

    override fun getRatingFilterVal(): String {
        return getCheckedChipValue(chipGroupFilterRating)
    }

    override fun orderByFilterVal(): String {
        return if (switchFilterOrderBy.isChecked) "asc" else "desc"
    }

    override fun showLoadingMoreIndicator() {
        viewLoadMore.visibility = View.VISIBLE
        vfLoadingIndicator.displayedChild = 0
        tvLoadMore.text = activity!!.getText(R.string.loading)
        tvLoadMore.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources!!.getDimension(R.dimen.all_6sp))
    }

    override fun hideLoadingMoreIndicator() {
        viewLoadMore.visibility = View.GONE
    }

    override fun showLoadingMoreErrorIndicator(errorMsg: String) {
        Timber.e("Error loading more $movieType: $errorMsg")
        Handler().postDelayed({ hideLoadingMoreIndicator() }, 2000)
        vfLoadingIndicator.displayedChild = 1
        tvLoadMore.text = errorMsg
        tvLoadMore.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources!!.getDimension(R.dimen.all_5sp))
    }


    override fun updateData(movies: MutableList<Movie>) {
        adapter.updateData(movies)
    }

    override fun getScreenCode(): String {
        return movieType
    }

    private fun checkChipByValue(chipGroup: ChipGroup, title: String) {
        for (i in 0 until chipGroup.childCount) {
            val childAt = chipGroup.getChildAt(i) as Chip
            if (title == childAt.text.toString())
                childAt.isChecked = true
        }
    }

    private fun getCheckedChipValue(chipGroup: ChipGroup): String {
        for (i in 0 until chipGroup.childCount) {
            val childAt = chipGroup.getChildAt(i) as Chip
            if (childAt.isChecked) return childAt.text.toString()
        }
        return ""
    }

    companion object {
        private const val TYPE = "MOVIE_TYPE"
    }

}