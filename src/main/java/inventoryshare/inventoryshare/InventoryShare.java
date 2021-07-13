package inventoryshare.inventoryshare;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryShare extends JavaPlugin implements Listener {

    private final ArrayList<Material> BlockList = new ArrayList<>();

    SharedInventory chestInven = new SharedInventory(54, "§0§l공용 상자");

    private HashMap<String, ArrayList<Integer>> toolMap = new HashMap<String, ArrayList<Integer>>();
    private HashMap<String, String> toolLimitMap = new HashMap<String, String>();

    public List<String> ingamePlayer = new ArrayList<String>();

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        ArrayList<Integer> sword_list = new ArrayList<Integer>();
        sword_list.add(268);
        sword_list.add(267);
        sword_list.add(272);
        sword_list.add(276);
        sword_list.add(283);

        ArrayList<Integer> hoe_list = new ArrayList<Integer>();
        hoe_list.add(290);
        hoe_list.add(291);
        hoe_list.add(292);
        hoe_list.add(293);
        hoe_list.add(294);

        ArrayList<Integer> pickaxe_list = new ArrayList<Integer>();
        pickaxe_list.add(257);
        pickaxe_list.add(270);
        pickaxe_list.add(274);
        pickaxe_list.add(278);
        pickaxe_list.add(285);

        ArrayList<Integer> axe_list = new ArrayList<Integer>();
        axe_list.add(258);
        axe_list.add(271);
        axe_list.add(275);
        axe_list.add(279);
        axe_list.add(286);

        ArrayList<Integer> shovel_list = new ArrayList<Integer>();
        shovel_list.add(256);
        shovel_list.add(269);
        shovel_list.add(273);
        shovel_list.add(277);
        shovel_list.add(284);

        ArrayList<Integer> hammer_list = new ArrayList<Integer>();
        hammer_list.add(5123);
        hammer_list.add(5124);
        hammer_list.add(5125);
        hammer_list.add(5126);
        hammer_list.add(5127);


        toolMap.put("칼", sword_list);
        toolMap.put("곡괭이", pickaxe_list);
        toolMap.put("삽", shovel_list);
        toolMap.put("괭이", hoe_list);
        toolMap.put("도끼", axe_list);
        toolMap.put("망치", hammer_list);


    }

    public void share_Inventory(Player players) {
        if(!ingamePlayer.contains(players.getUniqueId().toString())) return;

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if(!ingamePlayer.contains(p.getUniqueId().toString())) continue;
                if (p == players)
                    continue;
                p.getInventory().setArmorContents(players.getInventory().getArmorContents());
                p.getInventory().setContents(players.getInventory().getContents());
                p.updateInventory();
            }
        },1l);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
        if(commandLabel.equalsIgnoreCase("invenshare")){
            if(args.length == 0){
                sender.sendMessage("§a참여 대상의 이름을 입력해주세요.");
            } else {
                String target = args[0];
                OfflinePlayer offP = Bukkit.getOfflinePlayer(target);
                String uuid = offP.getUniqueId().toString();
                if(ingamePlayer.contains(uuid)){
                    ingamePlayer.remove(uuid);
                    sender.sendMessage("§a"+target+" 님은 이제 인벤토리를 공유하지 않습니다.");
                    if(offP.isOnline()){
                        Player t = (Player) offP;
                        t.sendMessage("§a당신은 이제 인벤토리가 공유되지 않습니다.");
                    }
                } else {
                    ingamePlayer.add(uuid);
                    sender.sendMessage("§a"+target+" 님은 이제 인벤토리를 공유합니다.");
                    if(offP.isOnline()){
                        Player t = (Player) offP;
                        t.sendMessage("§a당신은 이제 인벤토리가 공유됩니다.");
                    }
                }

            }
        }else if(commandLabel.equalsIgnoreCase("제한")){

            if(sender instanceof Player){
                Player p = (Player) sender;
                if(args.length == 0){
                    sender.sendMessage("§a제한 <칼/도끼/괭이/곡괭이/망치/삽>");
                } else {
                    String toolName = args[0];
                    String uuid = p.getUniqueId().toString();
                    if(toolName.equalsIgnoreCase("칼")){
                        toolLimitMap.put(uuid, toolName);
                        p.sendMessage("§c당신은 이제 [ "+toolName+" ] 도구만 사용할 수 있습니다.");
                    } else if(toolName.equalsIgnoreCase("도끼")){
                        toolLimitMap.put(uuid, toolName);
                        p.sendMessage("§c당신은 이제 [ "+toolName+" ] 도구만 사용할 수 있습니다.");
                    }else if(toolName.equalsIgnoreCase("괭이")){
                        toolLimitMap.put(uuid, toolName);
                        p.sendMessage("§c당신은 이제 [ "+toolName+" ] 도구만 사용할 수 있습니다.");
                    }else if(toolName.equalsIgnoreCase("곡괭이")){
                        toolLimitMap.put(uuid, toolName);
                        p.sendMessage("§c당신은 이제 [ "+toolName+" ] 도구만 사용할 수 있습니다.");
                    }else if(toolName.equalsIgnoreCase("망치")){
                        toolLimitMap.put(uuid, toolName);
                        p.sendMessage("§c당신은 이제 [ "+toolName+" ] 도구만 사용할 수 있습니다.");
                    }else if(toolName.equalsIgnoreCase("삽")){
                        toolLimitMap.put(uuid, toolName);
                        p.sendMessage("§c당신은 이제 [ "+toolName+" ] 도구만 사용할 수 있습니다.");
                    } else {
                        sender.sendMessage("§a제한 <칼/도끼/괭이/곡괭이/망치/삽>");
                    }
                }
            }


        }
        return true;
    }

    @EventHandler
    public void dropEvent(PlayerDropItemEvent e) {
        share_Inventory(e.getPlayer());
    }

    @EventHandler
    public void pickupEvent(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player)
            share_Inventory((Player)e.getEntity());
    }

    @EventHandler
    public void deathEvent(PlayerDeathEvent e) {
        share_Inventory(e.getEntity());
    }

    @EventHandler
    public void invClickEvent(InventoryClickEvent e) {
        share_Inventory((Player)e.getWhoClicked());
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent e) {
        share_Inventory(e.getPlayer());
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent e) {
        share_Inventory(e.getPlayer());
    }

    @EventHandler
    public void joinEvent(PlayerJoinEvent e) {
        //share_Inventory(e.getPlayer());
    }

    @EventHandler
    public void itemConsumeEvent(PlayerItemConsumeEvent e) {
        share_Inventory(e.getPlayer());
    }

    @EventHandler
    public void interactEvent(PlayerInteractEvent e) {
        if(ingamePlayer.contains(e.getPlayer().getUniqueId().toString())){
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
                if(e.getClickedBlock().getType() == Material.ITEM_FRAME){
                    e.setCancelled(true);
                    e.getPlayer().sendMessage("§c해당 블럭과의 상호작용은 불가능합니다.");
                    return;
                }
            }
        }
        share_Inventory(e.getPlayer());

        Player p = e.getPlayer();
        String uuid = p.getUniqueId().toString();
        if(toolLimitMap.containsKey(uuid)){
            String toolName = toolLimitMap.get(uuid);
            ArrayList<Integer> toolList = toolMap.get(toolName);
            ItemStack useItem = e.getItem();
            if(useItem == null) return;
            if(!toolList.contains(useItem.getTypeId())){ //자신의 툴 리스트에 포함되지 않고
                for(ArrayList<Integer> anotherToolList : toolMap.values()){
                    if(anotherToolList.contains(useItem.getTypeId())){ //다른 툴 리스트에 포함되있다면
                        p.sendMessage("§c당신은 이 아이템을 사용할 수 없습니다.");
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent e){
        Entity damager = e.getDamager();

        if(damager instanceof  Player){
            Player damagerP = (Player) e.getDamager();

            String uuid = damagerP.getUniqueId().toString();
            if(toolLimitMap.containsKey(uuid)){
                String toolName = toolLimitMap.get(uuid);
                ArrayList<Integer> toolList = toolMap.get(toolName);
                ItemStack useItem = damagerP.getInventory().getItemInMainHand();
                if(useItem == null) return;
                if(!toolList.contains(useItem.getTypeId())){
                    for(ArrayList<Integer> anotherToolList : toolMap.values()){
                        if(anotherToolList.contains(useItem.getTypeId())){ //다른 툴 리스트에 포함되있다면
                            damagerP.sendMessage("§c당신은 이 아이템을 사용할 수 없습니다.");
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void inventoryDragEvent(InventoryDragEvent e) {
        share_Inventory((Player)e.getWhoClicked());
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e){

        HumanEntity he = e.getPlayer();
        if(he instanceof Player){
            Player p = (Player) he;

            if(!ingamePlayer.contains(p.getUniqueId().toString())) return;

            if(e.getInventory().getType() == InventoryType.CHEST || e.getInventory().getType() == InventoryType.SHULKER_BOX){

                if(e.getInventory().getTitle().equalsIgnoreCase("§0§l공용 상자")) return;

                p.openInventory(chestInven.getInventory());
                e.setCancelled(true);
            }
        }
    }

}
