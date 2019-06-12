export default function(sequelize, DataTypes) {
    return sequelize.define(
        "review", // Model Name
        {
            email: {
                type: DataTypes.STRING,
                allowNull: false
            },
            review: {
                type: DataTypes.STRING,
                allowNull: false
            },
            createdAt: {
                type: DataTypes.DATE,
                defaultValue: DataTypes.NOW
            },
            updatedAt: {
                type: DataTypes.DATE,
                defaultValue: DataTypes.NOW,
                onUpdate: DataTypes.NOW
            }
        }
    );
}
