package MiscUtils.Utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 *
 * @author ProfMobius <Original Link: https://bitbucket.org/ProfMobius/waila/src/7c03246902710b7a92b0622f318ae654f4eb1f5b/src/main/java/mcp/mobius/waila/overlay/RayTracing.java?at=forge_1187>
 */


    public class RayTracing {

        private static RayTracing _instance;
        private RayTracing(){}
        public static RayTracing instance(){
            if(_instance == null)
                _instance = new RayTracing();
            return _instance;
        }

        private MovingObjectPosition target      = null;
        private ItemStack            targetStack = null;
        private Entity targetEntity= null;
        private Minecraft mc          = Minecraft.getMinecraft();

        public void fire(){
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY){
                this.target = mc.objectMouseOver;
                this.targetStack = null;
                return;
            }

            Entity viewpoint = mc.pointedEntity;
            if (viewpoint == null) return;

            this.target      = this.rayTrace(viewpoint, mc.playerController.getBlockReachDistance() - 0.5, 0);

            if (this.target == null) return;
        }

        public MovingObjectPosition getTarget(){
            return this.target;
        }

        public ItemStack getTargetStack(){
            if (this.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                this.targetStack = this.getIdentifierStack();
            else
                this.targetStack = null;

            return this.targetStack;
        }

        public Entity getTargetEntity(){
            if (this.target.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
                this.targetEntity = this.getIdentifierEntity();
            else
                this.targetEntity = null;

            return this.targetEntity;
        }

        public MovingObjectPosition rayTrace(Entity entity, double par1, float par3)
        {
            BlockPos pos     = entity.getPosition();
            Vec3 vec3 = new Vec3(pos.getX(), pos.getY(), pos.getZ());

            if (entity.getEyeHeight() != 0.12F)
                pos.add(0, entity.getEyeHeight(), 0);

            Vec3 vec31 = entity.getLook(par3);
            Vec3 vec32 = vec3.addVector(vec31.xCoord * par1, vec31.yCoord * par1, vec31.zCoord * par1);

                return entity.worldObj.rayTraceBlocks(vec3, vec32, false);
        }

        public ItemStack getIdentifierStack(){
            World world = mc.theWorld;
            ArrayList<ItemStack> items = this.getIdentifierItems();

            if (items.isEmpty())
                return null;

            Collections.sort(items, new Comparator<ItemStack>() {
                @Override
                public int compare(ItemStack stack0, ItemStack stack1) {
                    return stack1.getItemDamage() - stack0.getItemDamage();
                }
            });

            return items.get(0);
        }

        public Entity getIdentifierEntity(){
            ArrayList<Entity> ents = new ArrayList<Entity>();

            if (this.target == null)
                return null;

            if(ents.size() > 0)
                return ents.get(0);
            else
                return this.target.entityHit;
        }

        public ArrayList<ItemStack> getIdentifierItems()
        {
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();

            if (this.target == null)
                return items;

            EntityPlayer player = mc.thePlayer;
            World world = mc.theWorld;

            int x = this.target.func_178782_a().getX();
            int y = this.target.func_178782_a().getY();
            int z = this.target.func_178782_a().getZ();
            //int   blockID         = world.getBlockId(x, y, z);
            //Block mouseoverBlock  = Block.blocksList[blockID];
            Block mouseoverBlock  = world.getBlockState(new BlockPos(x, y, z)).getBlock();
            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
            if (mouseoverBlock == null) return items;


            if(items.size() > 0)
                return items;

            if (world.getTileEntity(new BlockPos(x, y, z)) == null){
                try{
                    ItemStack block = new ItemStack(mouseoverBlock, 1, 0);

                    if (block.getItem() != null)
                        items.add(block);


                } catch(Exception e){
                }
            }

            if(items.size() > 0)
                return items;

            try{
                ItemStack pick = mouseoverBlock.getPickBlock(this.target, world, new BlockPos(x, y, z));
                if(pick != null)
                    items.add(pick);
            }catch(Exception e){}

            if(items.size() > 0)
                return items;

        /*
        try
        {
            items.addAll(mouseoverBlock.getBlockDropped(world, x, y, z, world.getBlockMetadata(x, y, z), 0));
        }
        catch(Exception e){}

        if(items.size() > 0)
            return items;
        */

            if(mouseoverBlock instanceof IShearable)
            {
                IShearable shearable = (IShearable)mouseoverBlock;
                if(shearable.isShearable(new ItemStack(Items.shears), world, new BlockPos(x, y, z)))
                {
                    items.addAll(shearable.onSheared(new ItemStack(Items.shears), world, new BlockPos(x, y, z), 0));
                }
            }

            if(items.size() == 0)
                items.add(0, new ItemStack(mouseoverBlock, 1, 0));

            return items;
        }


    }
