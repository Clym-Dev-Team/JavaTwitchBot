import {InputHealth} from "./InputHealth.ts";
import "./HealthTile.css"

export interface HealthTileProps {
  input: InputHealth
}

export default function HealthTile({input}: HealthTileProps) {
  input.description = "lorem ipsum dolor sit amet, consectetur adipiscing"
  return <div className={"healthTile " + input.status}>
    <div className="firstRow">
      <span className="inputName">{input.name}</span>
      <span className="inputStatus">{input.status}</span>
    </div>
    <span className="inputDescription">{input.description}</span>
  </div>
}