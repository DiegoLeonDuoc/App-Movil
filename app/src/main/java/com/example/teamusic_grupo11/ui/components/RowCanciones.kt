package com.example.teamusic_grupo11.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.teamusic_grupo11.dataDAO.Song

@Composable
fun RowCanciones(songs: List<Song>, size: Dp, onSongClick: (Song) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(songs) { song ->
            CuadroCancion(song = song, onClick = { onSongClick(song) }, size = size)
        }
    }
}

@Composable
fun RowCancionesDoble(songs: List<Song>, size: Dp, onSongClick: (Song) -> Unit){
    // Group songs into chunks of 2 for the vertical layout
    val groupedSongs = songs.chunked(2)

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(groupedSongs) { pair ->
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Render each song in the pair
                pair.forEach { song ->
                    CuadroCancion(
                        song = song,
                        onClick = { onSongClick(song) },
                        size = size
                    )
                }
            }
        }
    }
}