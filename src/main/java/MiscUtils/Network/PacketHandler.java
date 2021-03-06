package MiscUtils.Network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;

import java.util.ArrayList;
import java.util.EnumMap;

public class PacketHandler {



    public static Packet GetPacket(AbstractPacket packet, EnumMap<Side, FMLEmbeddedChannel> channels){
        return channels.get(Side.SERVER).generatePacketFrom(packet);
    }


    public static void SendPacketToPlayerListExcept(AbstractPacket packet, EnumMap<Side, FMLEmbeddedChannel> channels, ArrayList<EntityPlayer> list, EntityPlayer pla){

        for(EntityPlayer pl : list) {

            if(pl.getCommandSenderName().equalsIgnoreCase(pla.getCommandSenderName()))
                continue;

            channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
            channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(pl);
            channels.get(Side.SERVER).writeAndFlush(packet);
        }
    }

    public static void SendPacketToPlayerList(AbstractPacket packet, EnumMap<Side, FMLEmbeddedChannel> channels, ArrayList<EntityPlayer> list){

        for(EntityPlayer pl : list) {

            channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
            channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(pl);
            channels.get(Side.SERVER).writeAndFlush(packet);
        }
    }


    public static void sendToAll(AbstractPacket packet, EnumMap<Side, FMLEmbeddedChannel> channels)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    public static void sendToPlayer(AbstractPacket packet, EntityPlayer player, EnumMap<Side, FMLEmbeddedChannel> channels)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    public static void sendToAllAround(AbstractPacket packet, NetworkRegistry.TargetPoint point, EnumMap<Side, FMLEmbeddedChannel> channels)
    {
       channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
       channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
       channels.get(Side.SERVER).writeAndFlush(packet);
    }

    public static void sendToDimension(AbstractPacket packet, int dimension, EnumMap<Side, FMLEmbeddedChannel> channels)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimension);
        channels.get(Side.SERVER).writeAndFlush(packet);
    }

    public static void sendToServer(AbstractPacket packet, EnumMap<Side, FMLEmbeddedChannel> channels)
    {
        channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channels.get(Side.CLIENT).writeAndFlush(packet);
    }

    public static void sendToAllExcept(AbstractPacket packet, EntityPlayer player, EnumMap<Side, FMLEmbeddedChannel> channels)
    {
        if(FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList != null && FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList.size() > 0)
        for(int i = 0; i < FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList.size(); i++)
        {
            EntityPlayer player1 = (EntityPlayer)FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList.get(i);

            if(player.getCommandSenderName().equalsIgnoreCase(player1.getCommandSenderName()))
            {
                continue;
            }

            PacketHandler.sendToPlayer(packet, player1, channels);
        }
    }




}
