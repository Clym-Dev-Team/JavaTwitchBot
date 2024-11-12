import {GiveawayStatus} from "../Giveaway.ts";

export interface GiveawayTileProps {
  isArchived?: boolean;
}

export default function GiveawayTile(props: GiveawayTileProps) {
  let rand = Math.trunc(Math.random() * 4);
  if (rand == 3) {
    rand = 4;
  }
  let status = GiveawayStatus[rand];
  if (props.isArchived) {
    status = GiveawayStatus[GiveawayStatus.ARCHIVED]
  }
  return <div className={"tile " + status}>
    <div className="firstRow">
      <span className="title">{"gwTitle"}</span>
      <span className="status">{status}</span>
    </div>
    <span className="description">{""}</span>
  </div>
}