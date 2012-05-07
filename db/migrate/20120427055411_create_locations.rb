class CreateLocations < ActiveRecord::Migration
  def change
    create_table :locations do |t|
      t.string :name
      t.decimal :lat
      t.decimal :lng
      t.integer :protect_level

      t.timestamps
    end
  end
end
