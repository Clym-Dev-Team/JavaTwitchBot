import {Input} from "../../../@shadcn/components/ui/input.tsx";
import {Button} from "../../../@shadcn/components/ui/button.tsx";
import IconCloudDown from "../../assets/IconCloudDown.tsx";
import VLabel from "../../common/VerticalLabel/VLabel.tsx";
import IconSave from "../../assets/IconSave.tsx";
import {useToast} from "../../../@shadcn/components/ui/use-toast.ts";
import {useState} from "react";
import {useForm} from "react-hook-form";
import {fetchWithAuth} from "../Login/LoginPage.tsx";
import {BOT_BACKEND_ADDR} from "../../main.tsx";

interface ChatterDTO {
  userId: string,
  coins: number,
  watchtimeSeconds: number,
}


export default function WatchtimeEditor() {
  const {toast} = useToast();
  const [loadedUser, setLoadedUser] = useState(false);
  const {handleSubmit, register, reset} = useForm<ChatterDTO>({ disabled: !loadedUser});

  function loadData() {
    fetchWithAuth(BOT_BACKEND_ADDR + "/watchtime/username").then()
      .then(response => response.json())
      .then(value => reset(value))
      .then(() => setLoadedUser(true))
      .catch(reason => toast({
        className: "toast toast-failure",
        title: "ERROR loading User",
        description: reason.toString()
      }))
  }

  function submit(c: ChatterDTO) {
    console.log("c: " + c);
  }

  return <div className="editTile">
    <h1>Edit Watchtime and Coins Data:</h1>
    <div className="searchBox">
      <Input placeholder="Search for twitch username..."/>
      <Button onClick={loadData}><IconCloudDown/></Button>
    </div>
    <div className="dataBox">
      <VLabel name="Watchtime [seconds]">
        <Input placeholder="..." {...register("watchtimeSeconds")}/>
      </VLabel>
      <VLabel name="Coins">
        <Input placeholder="..." {...register("coins")}/>
      </VLabel>
    </div>
    <Button onClick={handleSubmit(submit)}>
      <IconSave/>
    </Button>
    {/* TODO name input*/}
    {/* TODO load button*/}
    {/* TODO coin filed*/}
    {/* TODO watchtime field*/}
    {/* TODO save button*/}
    {/* TODO make save button only available after load*/}
  </div>

}