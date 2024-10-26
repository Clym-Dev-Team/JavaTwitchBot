import React from "react";
import "./IconCheckBox.css"
import {Tooltip, TooltipContent, TooltipTrigger} from "@shadcn/components/ui/tooltip.tsx";

export interface IconCheckBoxProps {
  checked: boolean;
  onChange: (checked: boolean) => void;
  icon: React.ReactNode;
  checkedIcon: React.ReactNode;
  hoverText?: string
}

export default function IconCheckBox(props: IconCheckBoxProps) {
  return <Tooltip>
    <TooltipContent>{props.hoverText}</TooltipContent>
    <TooltipTrigger>
      <div className="iconCheckBox" onClick={event => {
        event.stopPropagation();
        props.onChange(!props.checked)
      }}>
        {props.checked ? props.checkedIcon : props.icon}
      </div>
    </TooltipTrigger>
  </Tooltip>;
}