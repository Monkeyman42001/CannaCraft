package net.monkeyman42001.cannacraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.monkeyman42001.cannacraft.block.CannacraftModBlockEntities;
import net.monkeyman42001.cannacraft.component.Strain;

public class CannabisPlantBlockEntity extends BlockEntity {
	private Strain strain;

	public CannabisPlantBlockEntity(BlockPos pos, BlockState state) {
		super(CannacraftModBlockEntities.CANNABIS_PLANT.get(), pos, state);
	}

	public Strain getStrain() {
		return strain;
	}

	public void setStrain(Strain strain) {
		this.strain = strain;
		setChanged();
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (strain != null) {
			tag.putString("strain_name", strain.name());
			tag.putFloat("strain_thc", strain.thcPercentage());
			tag.putFloat("strain_terpene", strain.terpenePercentage());
		}
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("strain_name")) {
			String name = tag.getString("strain_name");
			float thc = tag.getFloat("strain_thc");
			float terpene = tag.getFloat("strain_terpene");
			this.strain = new Strain(name, thc, terpene);
		} else {
			this.strain = null;
		}
	}
}
