/*
공유용 인벤토리 클래스
 */

package inventoryshare.inventoryshare;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SharedInventory {

    private Inventory inventory;

    public SharedInventory(int invenSize, String title){

        inventory = Bukkit.createInventory(null, invenSize, title);

        for(int i = 0; i < invenSize; i++){
            inventory.setItem(i, new ItemStack(Material.AIR, 1));
        }

        Bukkit.broadcastMessage(inventory.toString());
    }

    public boolean isFull(){
        for(ItemStack tmpItem : inventory.getContents()){
            if(tmpItem == null || tmpItem.getType() == Material.AIR){
                return false;
            }
        }
        return true;
    }

    public Inventory getInventory(){
        return this.inventory;
    }

    
    /*
    모두 줬을 시 0 반환
     */
    public int addItem(ItemStack addingItem){

        int leftAmount = addingItem.getAmount(); //남은 수량

        if(isFull()){ //인벤 꽉찼으면 아이템 줄 수는 있는지 확인

            ItemStack comp1 = addingItem; //수량을 무시한 비교를 위해
            addingItem.setAmount(1);

            for(ItemStack tmpItem : inventory.getContents()){
                if(tmpItem != null){
                    ItemStack comp2 = tmpItem.clone();
                    comp1.setAmount(2);

                    if(comp1.equals(comp2)){
                        int canGiveAmout = 64 - tmpItem.getAmount(); //줄 수 있는 수량
                        if(canGiveAmout >= leftAmount){ //남은 수량 다 줄 수 있다면
                            addingItem.setAmount(leftAmount); //줄 아이템 수량 줘야 되는 수량만큼 설정
                            leftAmount = 0; //남은 수량 0으로

                            inventory.addItem(addingItem); //아이템 주기
                        } else {

                            addingItem.setAmount(canGiveAmout); //줄 수 있는 만큼 줄거임
                            leftAmount -= canGiveAmout; //준 수량만큼 남은 수량 차감

                            inventory.addItem(addingItem);
                        }
                    }
                }
            }

        } else { //꽉 안찼다면 그냥 주고 끝
            inventory.addItem(addingItem);
            leftAmount = 0;
        }

        return leftAmount; //남은 수량 반환

    }

}
