package com.example.teamusic_grupo11.data

import com.example.teamusic_grupo11.R
import com.example.teamusic_grupo11.dataDAO.Song

fun Songs(): List<Song> {
    return listOf(
        Song("Everlong", "Foo Fighters", R.drawable.logo),
        Song("Numb", "Linkin Park", R.drawable.logo),
        Song("Hysteria", "Muse", R.drawable.logo),
        Song("Smells Like Teen Spirit", "Nirvana", R.drawable.logo),
        Song("In the End", "Linkin Park", R.drawable.logo),
        Song("Supermassive Black Hole", "Muse", R.drawable.logo),
        Song("Californication", "Red Hot Chili Peppers", R.drawable.logo),
        Song("Boulevard of Broken Dreams", "Green Day", R.drawable.logo),
        Song("Chop Suey!", "System Of A Down", R.drawable.logo),
        Song("Karma Police", "Radiohead", R.drawable.logo),
        Song("Clocks", "Coldplay", R.drawable.logo),
        Song("Creep", "Radiohead", R.drawable.logo),
        Song("The Pretender", "Foo Fighters", R.drawable.logo),
        Song("Breaking the Habit", "Linkin Park", R.drawable.logo),
        Song("Starlight", "Muse", R.drawable.logo),
        Song("By the Way", "Red Hot Chili Peppers", R.drawable.logo),
        Song("American Idiot", "Green Day", R.drawable.logo),
        Song("Toxicity", "System Of A Down", R.drawable.logo),
        Song("Yellow", "Coldplay", R.drawable.logo)
    )

}