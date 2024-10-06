import React from "react";
import "./IconCheckBox.css"

export interface IconCheckBoxProps {
  checked: boolean;
  onChange: (checked: boolean) => void;
  icon: React.ReactNode;
  checkedIcon: React.ReactNode;
  hoverText?: string
}

export default function IconCheckBox(props: IconCheckBoxProps) {
  return <div className="iconCheckBox" onClick={() => props.onChange(!props.checked)}>
    {props.checked ? props.checkedIcon : props.icon}
  </div>;
}