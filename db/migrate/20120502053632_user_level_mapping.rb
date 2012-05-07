class UserLevelMapping < ActiveRecord::Migration
  def change    
    add_column :locations, :user_id, :int
    add_column :locations, :request_user_id, :int
  end
end
