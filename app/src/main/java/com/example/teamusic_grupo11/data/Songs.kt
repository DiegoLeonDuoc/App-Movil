package com.example.teamusic_grupo11.data

import com.example.teamusic_grupo11.dataDAO.Song

fun Songs(): List<Song> {
    return listOf(
        Song(
            title = "Everlong",
            artist = "Foo Fighters",
            imageRes = "https://lh3.googleusercontent.com/SP6xIjtaVOa9HYznOKdH-Ay59tZ_JSxF_ecD5aNDzuvt0LXzD1u1kt1ZPuSfeCIv2nNwTKAGDGXCtrU=w120-h120-l90-rj",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3", // URL de audio
            duration = 250000 // 4:10 minutos en milisegundos
        ),
        Song(
            title = "Numb",
            artist = "Linkin Park",
            imageRes = "https://lh3.googleusercontent.com/YhfFgdvtwfIcLi_2H54RP15T3IPiUZnNtlwvlomHsAmAMQMZhgXu11HoFWPcWAiJRfdChtrqrGRbQl3idA=w120-h120-l90-rj",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
            duration = 187000 // 3:07 minutos
        ),
        Song(
            title = "Hysteria",
            artist = "Muse",
            imageRes = "https://lh3.googleusercontent.com/pYoc6fyzEsIQhv9CB5YDlThePpQhPmh6wJBUZmzaK7Hge6wi47Aynu5UXI9FqLgvinFw5ccW6bodbXM=w120-h120-l90-rj",
            audioUrl = "https://soundcloud.com/muse/hysteria?utm_source=clipboard&utm_medium=text&utm_campaign=social_sharing",
            duration = 227000 // 3:47 minutos
        ),
        Song(
            title = "Smells Like Teen Spirit",
            artist = "Nirvana",
            imageRes = "https://lh3.googleusercontent.com/SP6xIjtaVOa9HYznOKdH-Ay59tZ_JSxF_ecD5aNDzuvt0LXzD1u1kt1ZPuSfeCIv2nNwTKAGDGXCtrU=w120-h120-l90-rj",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
            duration = 301000 // 5:01 minutos
        ),
        Song(
            title = "In the End",
            artist = "Linkin Park",
            imageRes = "https://lh3.googleusercontent.com/SP6xIjtaVOa9HYznOKdH-Ay59tZ_JSxF_ecD5aNDzuvt0LXzD1u1kt1ZPuSfeCIv2nNwTKAGDGXCtrU=w120-h120-l90-rj",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3",
            duration = 216000 // 3:36 minutos
        ),
        Song(
            title = "Supermassive Black Hole",
            artist = "Muse",
            imageRes = "https://lh3.googleusercontent.com/SP6xIjtaVOa9HYznOKdH-Ay59tZ_JSxF_ecD5aNDzuvt0LXzD1u1kt1ZPuSfeCIv2nNwTKAGDGXCtrU=w120-h120-l90-rj",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3",
            duration = 210000 // 3:30 minutos
        ),
        Song(
            title = "Californication",
            artist = "Red Hot Chili Peppers",
            imageRes = "https://lh3.googleusercontent.com/SP6xIjtaVOa9HYznOKdH-Ay59tZ_JSxF_ecD5aNDzuvt0LXzD1u1kt1ZPuSfeCIv2nNwTKAGDGXCtrU=w120-h120-l90-rj",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3",
            duration = 329000 // 5:29 minutos
        ),
        Song(
            title = "Boulevard of Broken Dreams",
            artist = "Green Day",
            imageRes = "https://lh3.googleusercontent.com/SP6xIjtaVOa9HYznOKdH-Ay59tZ_JSxF_ecD5aNDzuvt0LXzD1u1kt1ZPuSfeCIv2nNwTKAGDGXCtrU=w120-h120-l90-rj",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3",
            duration = 272000 // 4:32 minutos
        )
    )
}