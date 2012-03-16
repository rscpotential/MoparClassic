package org.moparscape.msc.gs.npchandler

import org.moparscape.msc.gs.model.dialog.NpcDialog
import org.moparscape.msc.gs.model.InvItem

class Tanner extends NpcDialog {

	override def init {
		this + option1
		this + option2
		this + option3
	}

	override def begin {
		this > "Greetings friend, I'm a manufacturer of leather."
		breath
		end
	}

	lazy val option1 = new GenericEnd("Can I buy some leather then?", npc, player) {
		override def begin {
			this > "I make leather from cow hides."
			breath
			this > "Bring me some and a gold per hide"
			super.begin
		}
	}

	lazy val option2 = new GenericEnd("Here's some cow hides, can I buy some leather now?", npc, player) {
		override def begin {
			this > "Ok"
			
			while (hasMaterials == 0) {
				player.getInventory.remove(147, 1)
				player.getInventory.remove(10, 1)
				this >> "The tanner tans your hide for 1gp."
				player.getInventory.add(new InvItem(148, 1))
				breath
				player.getActionSender.sendInventory
			}

			if (hasMaterials == 1) {
				this >> "You have run out of hides."
			} else if (hasMaterials == -1) {
				this >> "You have run out of coins."
			}

			super.begin
		}

		private def hasMaterials = {
			if (player.getInventory.countId(147) < 1) 1
			else if (player.getInventory.countId(10) < 1) -1
			else 0
		}
	}

	lazy val option3 = new GenericEnd("Leather is rather weak stuff", npc, player) {
		override def begin {
			super.begin
			this > "Well yes if all you're concerned with is how much it will protect you in a fight."
		}
	}

}