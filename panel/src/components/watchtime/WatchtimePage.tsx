import {Input} from "../../../@shadcn/components/ui/input.tsx";
import {Button} from "../../../@shadcn/components/ui/button.tsx";
import IconCloudDown from "../../assets/IconCloudDown.tsx";
import "./watchtime.css"
import VLabel from "../../common/VerticalLabel/VLabel.tsx";
import IconSave from "../../assets/IconSave.tsx";
import {Table, TableHead, TableHeader, TableRow} from "../../../@shadcn/components/ui/table.tsx";

export default function WatchtimePage() {
  return <div className="watchtimePage">
    <div className="leaderboard column">
      <h1>Top 100 Leaderboard - Watchtime:</h1>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead className="tw-w-10">#</TableHead>
            <TableHead>Username</TableHead>
            <TableHead className="tw-w-32">Coins</TableHead>
            <TableHead>Seconds</TableHead>
            <TableHead className="tw-w-32">Days</TableHead>
          </TableRow>
        </TableHeader>
      </Table>
    </div>
    <div className="column">
      <div className="editTile">
        <h1>Edit Watchtime and Coins Data:</h1>
        <div className="searchBox">
          <Input placeholder="Search for twitch username..."/>
          <Button><IconCloudDown/></Button>
        </div>
        <div className="dataBox">
          <VLabel name="Watchtime [seconds]">
            <Input placeholder="..."/>
          </VLabel>
          <VLabel name="Coins">
            <Input placeholder="..."/>
          </VLabel>
        </div>
        <Button>
          <IconSave/>
        </Button>
        {/* TODO name input*/}
        {/* TODO load button*/}
        {/* TODO coin filed*/}
        {/* TODO watchtime field*/}
        {/* TODO save button*/}
        {/* TODO make save button only available after load*/}
      </div>
    </div>
  </div>
}