package pl.kossa.myflights.architecture

abstract class BaseRecyclerViewModel<T>(
    val model: T
) : BaseObservable() {

    abstract fun onRowClick(model: T)
}