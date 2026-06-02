package jababarium.expand.block.special;

import arc.util.Time;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.liquid.LiquidJunction;
import mindustry.world.blocks.liquid.LiquidRouter;
import mindustry.world.blocks.liquid.LiquidBridge;

public class SelfHealingLiquidBlocks {

    public static class SelfHealingConduit extends Conduit {

        public float healPerSecond = 6f;

        public SelfHealingConduit(String name){
            super(name);

            liquidCapacity = 155f;
            liquidPressure = 2.5f;

            leaks = false;
        }

        public class SelfHealingConduitBuild extends ConduitBuild {

            @Override
            public void updateTile(){
                super.updateTile();
                healLogic();
            }

            void healLogic(){
                if(!damaged()) return;
                if(liquids.current() == null || liquids.currentAmount() <= 0.01f) return;

                float heal = healPerSecond * Time.delta;

                if(liquids.current().temperature > 0.7f){
                    heal *= 0.6f;
                }

                heal(heal);
            }
        }
    }

    public static class SelfHealingJunction extends LiquidJunction {

        public float healPerSecond = 8f;

        public SelfHealingJunction(String name){
            super(name);

            liquidCapacity = 160f;
        }

        public class SelfHealingJunctionBuild extends LiquidJunctionBuild {

            @Override
            public void updateTile(){
                super.updateTile();

                if(!damaged()) return;
                if(liquids.current() == null || liquids.currentAmount() <= 0.01f) return;

                float heal = healPerSecond * Time.delta;

                if(liquids.current().temperature > 0.7f){
                    heal *= 0.65f;
                }

                heal(heal);
            }
        }
    }

    public static class SelfHealingRouter extends LiquidRouter {

        public float healPerSecond = 7f;

        public SelfHealingRouter(String name){
            super(name);

            liquidCapacity = 190f;
        }

        public class SelfHealingRouterBuild extends LiquidRouterBuild {

            @Override
            public void updateTile(){
                super.updateTile();

                if(!damaged()) return;
                if(liquids.current() == null || liquids.currentAmount() <= 0.01f) return;

                float heal = healPerSecond * Time.delta;

                if(liquids.current().temperature > 0.7f){
                    heal *= 0.7f;
                }

                heal(heal);
            }
        }
    }

    public static class SelfHealingLiquidBridge extends LiquidBridge {

        public float healPerSecond = 6.5f;

        public SelfHealingLiquidBridge(String name){
            super(name);

            liquidCapacity = 190f;
            range = 8;
            hasPower = false;
        }

        public class SelfHealingLiquidBridgeBuild extends LiquidBridgeBuild {

            @Override
            public void updateTile(){
                super.updateTile();
                healLogic();
            }

            void healLogic(){
                if(!damaged()) return;
                if(liquids.current() == null || liquids.currentAmount() <= 0.01f) return;

                float heal = healPerSecond * Time.delta;

                if(liquids.current().temperature > 0.7f){
                    heal *= 0.65f;
                }

                heal(heal);
            }
        }
    }
}
