import tabStyle from './tab.module.css'
import React from 'react'
const Notes = ({ stream }) => (
	<div className={tabStyle.content}>
		{showNotesFor(stream)}
	</div>
)

const showNotesFor = stream => {
	switch (stream) {
		case "raiden": return raiden()
		case "reboot": return notesReboot()
		default: throw new Error("Unknown stream: " + stream)
	}
}

// Overlay test
const raiden = () => (
	<>
		<h1>Raiden - dev / code stream</h1>
		<p>Je suis dev et j'aime tester coder des trucs. </p>
		<p><b>!color red</b> change overlay color : green, red, orange, yellow, blue, blue_light, blue_dark, purple and pink !</p>
		<p>Welcome ! I'm currently testing and experimenting an overlay made in Java.</p>
		<p>Credits to Nicolaï Parlog</p>
	</>
)

const notesReboot = () => (
	<>
		<h1>Rebooting the Stream</h1>
		<p>My stream layout (i.e. the boxes you see on screen right now) is a Next.js web app. (OBS shows it via an embedded browser.) I've <em>not</em> been happy with the server-side development experience and want to migrate the app to Spring Boot & React. Since most of the existing code is just frontend React, that should be pretty straightforward (famous last words, I know), so let's do it!</p>
	</>
)

export default Notes
