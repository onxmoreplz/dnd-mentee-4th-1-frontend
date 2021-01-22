/**
 *  각각의 item을 recyclerview로 연결 시켜주는 adapter class
 */

package com.example.myapplication.navigation.upload

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.datasource.remote.api.RecipeDTO
import kotlinx.android.synthetic.main.recipe_list_item.view.*
import java.util.jar.Manifest

/**
 *  기존의 어댑터에서 '갤러리' 버튼 클릭 시 itemClick 이벤트가 생기게 추가하였습니다.
 */
class UploadRecipeAdapter(
    private val context: Context,
    private val recipeList: ArrayList<RecipeDTO.Recipe>,
    val itemClick: (Int, RecipeDTO.Recipe) -> Unit
) :
    RecyclerView.Adapter<UploadRecipeAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.recipe_list_item,
            parent, false
        )
        return RecipeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val element = recipeList[position]
        holder.bind(element)
        holder.recipeComment.setText(recipeList[position].comment)

        holder.itemView.setOnClickListener {
            Toast.makeText(
                context,
                "번호 : " + recipeList.get(position).number
                        + "내용 : " + recipeList.get(position).comment + "사진 : " + recipeList.get(position).image + "입니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recipeNumber: TextView = itemView.tv_number
        var recipeComment: EditText = itemView.et_comment
        private val recipePhoto: ImageView = itemView.iv_photo
        // private val recipeButton: Button = itemView.btn_gallery

        fun bind(data: RecipeDTO.Recipe) {
            recipeNumber.text = data.number

            if (data.image != "") {
                Glide.with(itemView)
                    .load(data.image)
                    .into(recipePhoto)
            } else {
                Glide.with(itemView)
                    .load(R.drawable.gallery)
                    .into(recipePhoto)
            }

            recipePhoto.setOnClickListener {
                itemClick(adapterPosition, data)
            }

            recipeComment.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    Log.d("Beforeposition", adapterPosition.toString())
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    Log.d("Onposition", adapterPosition.toString())
                }

                override fun afterTextChanged(p0: Editable?) {
                    recipeList[adapterPosition].comment = p0.toString()
                    Log.d("Afterposition", adapterPosition.toString())
                }
            })

        }
    }
}