//package com.myhss.AllShakha.Adapter
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.myhss.ui.Barchat.Model.ProductModel
//import com.uk.myhss.R
//
//class ProductListAdapter(
//    private val mContext: Context,
//    private val mOnProductClickListener: OnProductClickListener,
//    private val mProductList: ArrayList<ProductModel> = ArrayList()
//) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
//        val inflater = LayoutInflater.from(mContext)
//        val view = inflater.inflate(R.layout.field, parent, false)
//        val holder = ProductViewHolder(view)
//
//        // item view is the root view for each row
//        holder.itemView.setOnClickListener {
//
//            // adapterPosition give the actual position of the item in the RecyclerView
//            val position = holder.adapterPosition
//            val model = mProductList[position]
//
//            // remove the Rs. prefix before sending the model for editing
//            model.price = model.price.substringAfterLast(" ")
//            mOnProductClickListener.onUpdate(position, model)
//        }
//
//        // to delete the item in recycler view
//        holder.productDelete.setOnClickListener {
//            val position = holder.adapterPosition
//            val model = mProductList[position]
//            mOnProductClickListener.onDelete(model)
//        }
//
//        return holder
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//
//        // data will be set here whenever the system thinks it's required
//
//        // get the product at position
//        val product = mProductList[position]
//
//        holder.productName.text = product.name
//        holder.productPrice.text = "Rs. ${product.price}"
//    }
//
//    /**
//     * Returns the total number of items in the list to be displayed.
//     * this will refresh when we call notifyDataSetChanged() or other related methods.
//     */
//    override fun getItemCount(): Int {
//        return mProductList.size
//    }
//
//    /**
//     * Adds each item to list for recycler view.
//     */
//    fun addProduct(model: ProductModel) {
//        mProductList.add(model)
//        // notifyDataSetChanged() // this method is costly I avoid it whenever possible
//        notifyItemInserted(mProductList.size)
//    }
//
//    /**
//     * Updates the existing product at specific position of the list.
//     */
//    fun updateProduct(model: ProductModel?) {
//
//        if (model == null) return // we cannot update the value because it is null
//
//        for (item in mProductList) {
//            // search by id
//            if (item.id == model.id) {
//                val position = mProductList.indexOf(model)
//                mProductList[position] = model
//                notifyItemChanged(position)
//                break // we don't need to continue anymore
//            }
//        }
//    }
//
//    /**
//     * Removes the specified product from the list.
//     *
//     * @param model to be removed
//     */
//    fun removeProduct(model: ProductModel) {
//        val position = mProductList.indexOf(model)
//        mProductList.remove(model)
//        notifyItemRemoved(position)
//    }
//
//    fun getNextItemId(): Int {
//        var id = 1
//        if (mProductList.isNotEmpty()) {
//            // .last is equivalent to .size() - 1
//            // we want to add 1 to that id and return it
//            id = mProductList.last().id + 1
//        }
//        return id
//    }
//
//    /**
//     * ViewHolder implementation for holding the mapped views.
//     */
//    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val productName: TextView = itemView.findViewById(R.id.product_name)
//        val productPrice: TextView = itemView.findViewById(R.id.product_price)
//        val productDelete: ImageView = itemView.findViewById(R.id.delete_product)
//    }
//
//}