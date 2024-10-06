import React from "react";
import "./TestingPopupBackground.css"
export interface TestingPopupBackgroundProps {
  content?: React.ReactNode;
}

export default function TestingPopupBackground(props: TestingPopupBackgroundProps) {
  return <div id="testingPopupBackground">
    <div id="spacer"/>
    {props.content}
  </div>
}