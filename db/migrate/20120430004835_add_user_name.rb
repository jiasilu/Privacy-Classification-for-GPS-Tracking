class AddUserName < ActiveRecord::Migration
  def up  
    add_column :users, :user_name, :string
  end

  def down  
    remove_column :users, :user_name, :string
  end
end
