import {Tabs, TabsContent, TabsList, TabsTrigger} from "../../../@shadcn/components/ui/tabs.tsx";
import CommandList from "./commands/CommandList.tsx";
import TemplateListPane from "../Templates/TemplateList/TemplateListPane.tsx";
import "./CommandPage.css"

export default function CommandsPage() {
  return <Tabs defaultValue="commands" >
    <TabsList className="commandTabList">
      <TabsTrigger value="commands">Commands</TabsTrigger>
      <TabsTrigger value="templates">Templates</TabsTrigger>
      <TabsTrigger value="trigger">Trigger</TabsTrigger>
    </TabsList>
    <TabsContent value="commands">
      <CommandList/>
    </TabsContent>
    <TabsContent value="templates">
      <TemplateListPane/>
    </TabsContent>
    <TabsContent value="trigger">
      <p>Trigger PlaceHolder</p>
    </TabsContent>
  </Tabs>
}