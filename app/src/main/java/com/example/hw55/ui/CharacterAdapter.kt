package com.example.hw55.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorti.R
import com.example.rickandmorti.databinding.ItemCharacterBinding


class CharacterAdapter() : ListAdapter<CharacterEntity, CharacterAdapter.CharacterViewHolder>(diffUtil) {

    inner class CharacterViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(character: CharacterEntity) = with(binding) {
            characterName.text = character.name
            characterLocation.text = character.status // Измените для отображения нужной локации
            characterFirstSeen.text = character.status // Измените для отображения нужного источника
            imgCharacter.load(base64ToBitmap(character.imageBase64)) {
                crossfade(true)
            }
            characterStatus.text = character.status
            colorIndicator.setImageResource(
                when {
                    character.status.contains("Dead", ignoreCase = true) -> R.drawable.ic_circle_red
                    character.status.contains("Alive", ignoreCase = true) -> R.drawable.ic_circle_green
                    else -> R.drawable.ic_circle_gray
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        getItem(position)?.let {
            holder.onBind(it)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CharacterEntity>() {
            override fun areItemsTheSame(oldItem: CharacterEntity, newItem: CharacterEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CharacterEntity, newItem: CharacterEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
