package mods.battlegear2.client.renderer;

import mods.battlegear2.client.utils.ImageCache;
import mods.battlegear2.heraldry.TileEntityFlagPole;
import mods.battlegear2.utils.BattlegearConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * User: nerd-boy
 * Date: 2/08/13
 * Time: 2:33 PM
 * TODO: Add discription
 */
public class FlagPoleTileRenderer extends TileEntitySpecialRenderer {
    public static final int period = 250;
    public static final int flag_sections = 16;
    public static double getZLevel(float x, float size, long time){
        return Math.pow(x, 0.5/(size/5)) * Math.sin(Math.PI * ( -x/size * 3 + ((float)(time% period)) / (0.5F*(float)period))) / 4;
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {

        if(tileentity instanceof TileEntityFlagPole){
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            int type = tileentity.getBlockMetadata();
            int side = BattlegearConfig.banner.getOrient(tileentity.getBlockMetadata());

            GL11.glPushMatrix();
            GL11.glTranslated(d0, d1, d2);
            GL11.glColor3f(1,1,1);

            switch (side){
                case 0:
                    renderYFlagPole(tileentity, d0, d1, d2, f, type, side);
                    renderYFlag(tileentity, d0, d1, d2, f, type, side);
                    break;
                case 1:
                    renderZFlagPole(tileentity, d0, d1, d2, f, type, side);
                    renderZFlag(tileentity, d0, d1, d2, f, type, side);
                    break;
                case 2:
                    GL11.glRotatef(90, 0, 1, 0);
                    GL11.glTranslatef(-1, 0, 0);
                    renderZFlagPole(tileentity, d0, d1, d2, f, type, side);
                    renderZFlag(tileentity, d0, d1, d2, f, type, side);
                    break;
            }

            GL11.glPopMatrix();
        }
    }

    private void renderZFlag(TileEntity tileentity, double d0, double d1, double d2, float f, int type, int side) {

        Tessellator tess = Tessellator.instance;

        if(((TileEntityFlagPole) tileentity).hasFlag())
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            List<ItemStack> flags = ((TileEntityFlagPole) tileentity).getFlags();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            GL11.glRotatef(-90, 0, 0 , 1);
            for(int flagIndex = 0; flagIndex < flags.size(); flagIndex++){
                ItemStack flag = flags.get(flagIndex);
                ImageCache.setTexture(flag);

                if(flag_sections == 0){
                    tess.startDrawingQuads();

                    tess.addVertexWithUV(8F / 16F, -flagIndex+1-2F/16F, 0, 0, 0.001);
                    tess.addVertexWithUV(8F /16F, -flagIndex-2F/16F,0, 1.00, 0.001);
                    tess.addVertexWithUV(8F /16F, -flagIndex-2F/16F, 1, 1.00, 0.999);
                    tess.addVertexWithUV(8F / 16F, -flagIndex+1-2F/16F, 1, 0, 0.999);

                    tess.addVertexWithUV(8F / 16F, -flagIndex+1-2F/16F, 1, 0, 0.999);
                    tess.addVertexWithUV(8F /16F, -flagIndex-2F/16F, 1, 1.00, 0.999);
                    tess.addVertexWithUV(8F /16F, -flagIndex-2F/16F,0, 1.00, 0.001);
                    tess.addVertexWithUV(8F / 16F, -flagIndex+1-2F/16F, 0, 0, 0.001);


                    tess.draw();

                }else{

                    long time = System.currentTimeMillis();
                    for(int i = 0; i < flag_sections; i++){
                        tess.startDrawingQuads();

                        double z1 = getZLevel((float)((flag_sections - i)) / (float)flag_sections + flagIndex, 5, time) / 5F;
                        double z2 = getZLevel((float)(flag_sections - i+1) / (float)flag_sections + flagIndex, 5, time) / 5F;

                        tess.addVertexWithUV(8F/16F+z1, -flagIndex+(float)(i+1) / (float)flag_sections-2F/16F, 0,(float)(i+1) / (float)flag_sections, 0.999);
                        tess.addVertexWithUV(8F/16F+z2, -flagIndex+(float)(i) / (float)flag_sections-2F/16F, 0,(float)(i) / (float)flag_sections, 0.999);
                        tess.addVertexWithUV(8F/16F+z2, -flagIndex+(float)(i) / (float)flag_sections-2F/16F, 1,(float)(i) / (float)flag_sections, 0.001);
                        tess.addVertexWithUV(8F/16F+z1, -flagIndex+(float)(i+1) / (float)flag_sections-2F/16F, 1,(float)(i+1) / (float)flag_sections, 0.001);

                        tess.addVertexWithUV(8F/16F+z1, -flagIndex+(float)(i+1) / (float)flag_sections-2F/16F, 1,(float)(i+1) / (float)flag_sections, 0.001);
                        tess.addVertexWithUV(8F/16F+z2, -flagIndex+(float)(i) / (float)flag_sections-2F/16F, 1,(float)(i) / (float)flag_sections, 0.001);
                        tess.addVertexWithUV(8F/16F+z2, -flagIndex+(float)(i) / (float)flag_sections-2F/16F, 0,(float)(i) / (float)flag_sections, 0.999);
                        tess.addVertexWithUV(8F/16F+z1, -flagIndex+(float)(i+1) / (float)flag_sections-2F/16F, 0,(float)(i+1) / (float)flag_sections, 0.999);


                        tess.draw();
                    }

                }

            }

            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    private void renderZFlagPole(TileEntity tileentity, double d0, double d1, double d2, float f, int type, int side) {
        IIcon icon = BattlegearConfig.banner.getIcon(2, type);
        Tessellator tess = Tessellator.instance;

        tess.startDrawingQuads();
        tess.addVertexWithUV(9F / 16F, 14F/16F, 0F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 0)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 0)));
        tess.addVertexWithUV(9F / 16F, 16F/16F, 0F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 1)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 0)));
        tess.addVertexWithUV(9F / 16F, 16F/16F, 16F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 1)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 4)));
        tess.addVertexWithUV(9F / 16F, 14F/16F, 16F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 0)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 4)));
        tess.draw();

        tess.startDrawingQuads();
        tess.addVertexWithUV(7F / 16F, 14F/16F, 0F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 2)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 0)));
        tess.addVertexWithUV(9F / 16F, 14F/16F, 0F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 1)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 0)));
        tess.addVertexWithUV(9F / 16F, 14F/16F, 16F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 1)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 4)));
        tess.addVertexWithUV(7F / 16F, 14F/16F, 16F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 2)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 4)));
        tess.draw();

        tess.startDrawingQuads();
        tess.addVertexWithUV(7F / 16F, 14F/16F, 16F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 2)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 4)));
        tess.addVertexWithUV(7F / 16F, 16F/16F, 16F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 3)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 4)));
        tess.addVertexWithUV(7F / 16F, 16F/16F, 0F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 3)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 0)));
        tess.addVertexWithUV(7F / 16F, 14F/16F, 0F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 2)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 0)));
        tess.draw();


        tess.startDrawingQuads();
        tess.addVertexWithUV(7F / 16F, 16F/16F, 16F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 2)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 4)));
        tess.addVertexWithUV(9F / 16F, 16F/16F, 16F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 1)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 4)));
        tess.addVertexWithUV(9F / 16F, 16F/16F, 0F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 1)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 0)));
        tess.addVertexWithUV(7F / 16F, 16F/16F, 0F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 2)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 0)));
        tess.draw();


        icon = BattlegearConfig.banner.getIcon(0, type);

        tess.startDrawingQuads();
        tess.addVertexWithUV(7F / 16F, 16F/16F, 0F / 16F, icon.getInterpolatedU(10), icon.getInterpolatedV(10));
        tess.addVertexWithUV(9F / 16F, 16F/16F, 0F / 16F, icon.getInterpolatedU(6), icon.getInterpolatedV(10));
        tess.addVertexWithUV(9F / 16F, 14F/16F, 0F / 16F, icon.getInterpolatedU(6), icon.getInterpolatedV(6));
        tess.addVertexWithUV(7F / 16F, 14F/16F, 0F / 16F, icon.getInterpolatedU(10), icon.getInterpolatedV(6));
        tess.draw();


        tess.startDrawingQuads();
        tess.addVertexWithUV(7F / 16F, 14F/16F, 16F / 16F, icon.getInterpolatedU(10), icon.getInterpolatedV(6));
        tess.addVertexWithUV(9F / 16F, 14F/16F, 16F / 16F, icon.getInterpolatedU(6), icon.getInterpolatedV(6));
        tess.addVertexWithUV(9F / 16F, 16F/16F, 16F / 16F, icon.getInterpolatedU(6), icon.getInterpolatedV(10));
        tess.addVertexWithUV(7F / 16F, 16F/16F, 16F / 16F, icon.getInterpolatedU(10), icon.getInterpolatedV(10));
        tess.draw();


    }


    private void renderYFlag(TileEntity tileentity, double d0, double d1, double d2, float f, int type, int side) {

        Tessellator tess = Tessellator.instance;

        if(((TileEntityFlagPole) tileentity).hasFlag())
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            List<ItemStack> flags = ((TileEntityFlagPole) tileentity).getFlags();


            GL11.glDisable(GL11.GL_LIGHTING);
            for(int flagIndex = 0; flagIndex < flags.size(); flagIndex++){
                ItemStack flag = flags.get(flagIndex);
                ImageCache.setTexture(flag);


                if(flag_sections == 0){
                    tess.startDrawingQuads();
                    tess.addVertexWithUV(7F / 16F- flagIndex, 0, 8F / 16F, 0, 0.999);
                    tess.addVertexWithUV(7F /16F- flagIndex - 1, 0, 8F / 16F, 1.0025, 0.999);
                    tess.addVertexWithUV(7F /16F- flagIndex - 1, 1, 8F / 16F, 1.0025, 0.001);
                    tess.addVertexWithUV(7F / 16F- flagIndex, 1, 8F / 16F, 0, 0.001);

                    tess.addVertexWithUV(7F / 16F- flagIndex, 1, 8F / 16F, 0, 0.001);
                    tess.addVertexWithUV(7F /16F- flagIndex - 1, 1, 8F / 16F, 1.0025, 0.001);
                    tess.addVertexWithUV(7F /16F- flagIndex - 1, 0, 8F / 16F, 1.0025, 0.999);
                    tess.addVertexWithUV(7F / 16F- flagIndex, 0, 8F / 16F, 0, 0.999);

                    tess.draw();

                }else{
                    long time = System.currentTimeMillis();
                    for(int i = 0; i < flag_sections; i++){
                        tess.startDrawingQuads();

                        double z1 = getZLevel((float)(i) / (float)flag_sections + flagIndex, 3, time);
                        double z2 = getZLevel((float)(i+1) / (float)flag_sections + flagIndex, 3, time);

                        tess.addVertexWithUV(7F / 16F-(float)(i) / (float)flag_sections - flagIndex, 0, 8F / 16F+z1, (float)(i) / (float)flag_sections, 0.999);
                        tess.addVertexWithUV(7F /16F- (float)(i+1) / (float)flag_sections- flagIndex, 0, 8F / 16F+z2, (float)(i+1) / (float)flag_sections, 0.999);
                        tess.addVertexWithUV(7F /16F-(float)(i+1) / (float)flag_sections- flagIndex, 1.0025, 8F / 16F+z2, (float)(i+1) / (float)flag_sections, 0.001);
                        tess.addVertexWithUV(7F / 16F- (float)(i) / (float)flag_sections- flagIndex, 1.0025, 8F / 16F+z1, (float)(i) / (float)flag_sections, 0.001);

                        tess.addVertexWithUV(7F / 16F- (float)(i) / (float)flag_sections - flagIndex, 1.0025, 8F / 16F+z1, (float)(i) / (float)flag_sections, 0.001);
                        tess.addVertexWithUV(7F /16F-(float)(i+1) / (float)flag_sections - flagIndex, 1.0025, 8F / 16F+z2, (float)(i+1) / (float)flag_sections, 0.001);
                        tess.addVertexWithUV(7F /16F- (float)(i+1) / (float)flag_sections - flagIndex, 0, 8F / 16F+z2, (float)(i+1) / (float)flag_sections, 0.999);
                        tess.addVertexWithUV(7F / 16F-(float)(i) / (float)flag_sections - flagIndex, 0, 8F / 16F+z1, (float)(i) / (float)flag_sections, 0.999);

                        tess.draw();
                    }
                }
            }
            GL11.glEnable(GL11.GL_LIGHTING);



            GL11.glDisable(GL11.GL_BLEND);

        }
    }

    private void renderYFlagPole(TileEntity tileentity, double d0, double d1, double d2, float f, int type, int side) {

        IIcon icon = BattlegearConfig.banner.getIcon(2, type);
        Tessellator tess = Tessellator.instance;

        tess.startDrawingQuads();
        tess.addVertexWithUV(7F / 16F, 0, 9F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 0)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 0)));
        tess.addVertexWithUV(9F / 16F, 0, 9F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 1)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 0)));
        tess.addVertexWithUV(9F / 16F, 1, 9F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 1)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 4)));
        tess.addVertexWithUV(7F / 16F, 1, 9F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 0)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 4)));
        tess.draw();

        tess.startDrawingQuads();
        tess.addVertexWithUV(9F / 16F, 0, 9F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 1)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 0)));
        tess.addVertexWithUV(9F / 16F, 0, 7F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 2)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 0)));
        tess.addVertexWithUV(9F / 16F, 1, 7F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 2)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 4)));
        tess.addVertexWithUV(9F / 16F, 1, 9F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 1)),icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 4)));
        tess.draw();


        tess.startDrawingQuads();
        tess.addVertexWithUV(9F / 16F, 0, 7F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 2)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 0)));
        tess.addVertexWithUV(7F / 16F, 0, 7F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 3)),icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 0)));
        tess.addVertexWithUV(7F / 16F, 1, 7F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 3)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 4)));
        tess.addVertexWithUV(9F / 16F, 1, 7F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 2)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 4)));
        tess.draw();


        tess.startDrawingQuads();
        tess.addVertexWithUV(7F / 16F, 0, 7F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 3)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 0)));
        tess.addVertexWithUV(7F / 16F, 0, 9F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 4)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 0)));
        tess.addVertexWithUV(7F / 16F, 1, 9F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 4)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 4)));
        tess.addVertexWithUV(7F / 16F, 1, 7F / 16F, icon.getInterpolatedU(BattlegearConfig.banner.getTextDim(type, 3)), icon.getInterpolatedV(BattlegearConfig.banner.getTextDim(type, 4)));
        tess.draw();

        icon = BattlegearConfig.banner.getIcon(0, type);

        tess.startDrawingQuads();
        tess.addVertexWithUV(7F / 16F, 0, 7F / 16F, icon.getInterpolatedU(6), icon.getInterpolatedV(6));
        tess.addVertexWithUV(9F / 16F, 0, 7F / 16F, icon.getInterpolatedU(10), icon.getInterpolatedV(6));
        tess.addVertexWithUV(9F / 16F, 0, 9F / 16F, icon.getInterpolatedU(10), icon.getInterpolatedV(10));
        tess.addVertexWithUV(7F / 16F, 0, 9F / 16F, icon.getInterpolatedU(6), icon.getInterpolatedV(10));
        tess.draw();

        tess.startDrawingQuads();
        tess.addVertexWithUV(9F / 16F, 1, 7F / 16F, icon.getInterpolatedU(6), icon.getInterpolatedV(6));
        tess.addVertexWithUV(7F / 16F, 1, 7F / 16F, icon.getInterpolatedU(10), icon.getInterpolatedV(6));
        tess.addVertexWithUV(7F / 16F, 1, 9F / 16F, icon.getInterpolatedU(10), icon.getInterpolatedV(10));
        tess.addVertexWithUV(9F / 16F, 1, 9F / 16F, icon.getInterpolatedU(6), icon.getInterpolatedV(10));
        tess.draw();
    }

}