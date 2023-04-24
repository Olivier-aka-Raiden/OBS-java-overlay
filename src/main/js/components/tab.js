import style from './tab.module.css'
import React from 'react'
// `name` is needed for the surrounding window to determine the tab tile
const Tab = ({ name, className, children }) => (
	<div className={`${style.tab} ${className}`}>
		{children}
	</div>
)

export default Tab
