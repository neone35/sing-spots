# Sing spots

## Project Overview
Find places where you can record your own music. This app shows them in convenient way – on a single map, so you can easily find one nearest to you. Database of places is made up of all the past recording places of all the known artists and bands!

## Why this Project
This project was received as a task for Android Developer position. The exact app requirements were as follows: 
- Must be written in Java and/or Kotlin
- Use [MusicBrainz API](https://wiki.musicbrainz.org/Development)
- Places returned per request should be limited, but all places must be displayed on map. For example there 100 places for search term, but limit is 20, so you need 5 request to get all the places. Make this limit easy to tune in code
- Displayed places should be open from 1990
- Every pin has a lifespan, meaning after it expires, pin should be removed from the map. Lifespan calculation: open_year - 1990 = lifespan_in_seconds. Example: 2017 - 1990 = 27s.

## Screenshots
coming soon

## What Did I Learn / Use?
- Google Maps API
- MusicBrainz API
