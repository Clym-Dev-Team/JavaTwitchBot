import {Label} from "../../../@shadcn/components/ui/label.tsx";
import React from "react";

export interface VLabelProps {
  name: string,
  children: React.ReactNode
}

export default function VLabel(props: VLabelProps) {
  return <Label>
    {props.name}
    <div className="labelChildContainer" style={{paddingTop: "0.5rem"}}>
      {props.children}
    </div>
  </Label>
}